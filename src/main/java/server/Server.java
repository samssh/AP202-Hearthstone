package server;

import requests.Request;
import response.*;
import client.Client;
import hibernate.Connector;
import model.account.Deck;
import model.account.Player;
import model.log.*;
import model.main.*;
import util.Config;
import util.ConfigFactory;
import util.Loop;
import util.ModelLoader;
import view.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Server {
    static int STARTING_MANA;
    static int MANA_PER_TURN;
    static int CARD_PER_TURN;
    static int MAX_DECK_SIZE;
    static int STARTING_PASSIVES;
    static int STARTING_HAND_CARDS;
    static int MAX_MANA;
    static int STARTING_COINS;
    static int MAX_DECK_NUMBER;

    static {
        Config config = ConfigFactory.getInstance().getConfig("SERVER_CONFIG");
        STARTING_MANA = config.getProperty(Integer.class, "STARTING_MANA");
        MANA_PER_TURN = config.getProperty(Integer.class, "MANA_PER_TURN");
        CARD_PER_TURN = config.getProperty(Integer.class, "CARD_PER_TURN");
        MAX_DECK_SIZE = config.getProperty(Integer.class, "MAX_DECK_SIZE");
        STARTING_PASSIVES = config.getProperty(Integer.class, "STARTING_PASSIVES");
        STARTING_HAND_CARDS = config.getProperty(Integer.class, "STARTING_HAND_CARDS");
        MAX_MANA = config.getProperty(Integer.class, "MAX_MANA");
        STARTING_COINS = config.getProperty(Integer.class, "STARTING_COINS");
        MAX_DECK_NUMBER = config.getProperty(Integer.class, "MAX_DECK_NUMBER");
    }

    private static final Server instance = new Server();
    private final List<Request> tempRequestList, requestList;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Loop executor;
    private Player player;
    private Game game;
    private final Collection collection;
    private final Shop shop;
    // last filter of collection


    public static Server getInstance() {
        return instance;
    }

    private Server() {
        tempRequestList = new ArrayList<>();
        requestList = new ArrayList<>();
        connector = new Connector(ConfigFactory.getInstance().getConfigFile("SERVER_HIBERNATE_CONFIG")
                , "server", System.getenv("HearthStone password"));
        modelLoader = new ModelLoader(connector);
        collection = new Collection(connector, modelLoader);
        shop = new Shop(connector, modelLoader);
        executor = new Loop(60, this::executeRequests);
        executor.start();

    }

    private void executeRequests() {
        synchronized (tempRequestList) {
            requestList.addAll(tempRequestList);
            tempRequestList.clear();
        }
        requestList.forEach(Request::execute);
        requestList.clear();
    }

    public void addRequest(Request request) {
        if (request != null) {
            synchronized (tempRequestList) {
                tempRequestList.add(request);
            }
            connector.save(new RequestLog(request, player == null ? null : player.getUserName()));
        }
    }

    public void shutdown() {
        executor.stop();
        executeRequests();
        connector.close();
    }

    public void login(String username, String password, int mode) {
        if (mode == 1)
            signIn(username, password);
        if (mode == 2)
            signUp(username, password);
    }

    private void signIn(String userName, String password) {
        Player p = connector.fetch(Player.class, userName);
        if (p != null) {
            if (p.getPassword().equals(password)) {
                this.player = p;
                Response response = new LoginResponse(true, player.getUserName());
                Client.getInstance().putAnswer(response);
                connector.save(new ResponseLog(response, player.getUserName()));
                connector.save(new AccountLog(player.getUserName(), "sign in"));
            } else {
                Response response = new LoginResponse(false, "wrong password");
                Client.getInstance().putAnswer(response);
                connector.save(new ResponseLog(response, p.getUserName()));
            }
        } else {
            Response response = new LoginResponse(false, "username not exist");
            Client.getInstance().putAnswer(response);
            connector.save(new ResponseLog(response, null));
        }
    }

    private void signUp(String username, String password) {
        Player fetch = connector.fetch(Player.class, username);
        if (fetch == null) {
            fetch = new Player(username, password, System.currentTimeMillis(), STARTING_COINS, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            connector.save(new HeaderLog(fetch.getCreatTime(), fetch.getUserName(), fetch.getPassword()));
            connector.save(fetch);
            this.player = fetch;
            Response response = new LoginResponse(true, this.player.getUserName());
            Client.getInstance().putAnswer(response);
            connector.save(new ResponseLog(response, this.player.getUserName()));
            connector.save(new AccountLog(this.player.getUserName(), "sign up"));
        } else {
            Response response = new LoginResponse(false, "username already exist");
            Client.getInstance().putAnswer(response);
            connector.save(new ResponseLog(response, null));
        }
    }

    public void logout() {
        if (this.player != null) {
            connector.save(player);
            connector.save(new AccountLog(player.getUserName(), "logout"));
            this.player = null;
        }
    }

    public void deleteAccount() {
        if (this.player != null) {
            connector.delete(player);
            connector.save(new AccountLog(player.getUserName(), "delete account"));
            HeaderLog h = connector.fetch(HeaderLog.class, player.getCreatTime());
            h.setDeletedAt(System.currentTimeMillis() + "");
            connector.save(h);
            this.player = null;
        }
    }

    public void sendShop() {
        shop.sendShop(player);
    }

    public void sellCard(String cardName) {
        shop.sellCard(cardName,player);
    }

    public void buyCard(String cardName) {
        shop.buyCard(cardName,player);
    }


    public void sendStatus() {
        Response response = new StatusDetails(makeStatusDetails());
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    private List<BigDeckOverview> makeStatusDetails() {
        return player.getDecks().stream().sorted(Comparator.comparing(Deck::getWinRate)
                .thenComparing(Deck::getWins).thenComparing(Deck::getManaAverage).thenComparing(Deck::getName))
                .map(this::createBigDeckOverview).collect(Collectors.toList());
    }

    private BigDeckOverview createBigDeckOverview(Deck deck) {
        return new BigDeckOverview(deck, (getMVC(deck).map(Card::getName).orElse(null)));
    }

    private Optional<Card> getMVC(Deck deck) {
        Map<Card, CardDetails> map = deck.getCards();
        List<Card> result = new ArrayList<>();
        AtomicInteger max = new AtomicInteger();
        map.values().stream().max(Comparator.comparing(CardDetails::getRepeatedTimes))
                .ifPresent(cardDetails -> max.set(cardDetails.getRepeatedTimes()));
        map.keySet().stream().filter(c -> map.get(c).getUsage() == max.get()).forEach(result::add);
        return result.stream().max(Comparator.comparing(Card::getRarity).thenComparing(Card::getManaFrz)
                .thenComparing(Card::getInstanceValue).thenComparing(Card::getName));
    }

    public void sendFirstCollection() {
        collection.sendFirstCollection(player);
    }


    public void sendCollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        collection.sendCollectionDetails(name, classOfCard, mana, lockMode, deckName, player);
    }

    public void newDeck(String deckName, String heroName) {
        collection.newDeck(deckName, heroName, player);
    }

    public void deleteDeck(String deckName) {
        collection.deleteDeck(deckName, player);
    }

    public void changeDeckName(String oldDeckName, String newDeckName) {
        collection.changeDeckName(oldDeckName, newDeckName, player);
    }

    public void changeHeroDeck(String deckName, String heroName) {
        collection.changeHeroDeck(deckName, heroName, player);
    }

    public void removeCardFromDeck(String cardName, String deckName) {
        collection.removeCardFromDeck(cardName, deckName, player);
    }

    public void addCardToDeck(String cardName, String deckName) {
        collection.addCardToDeck(cardName, deckName, player, shop);
    }

    public void startPlay() {
        Response response;
        if (canStartGame()) {
            List<Passive> passives = chooseRandom(modelLoader.getFirstPassives(), STARTING_PASSIVES);
            response = new PassiveDetails(turnToPassiveOverview(passives));
        } else {
            response = new GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));

    }

    private boolean canStartGame() {
        Deck d = player.getSelectedDeck();
        return d != null && d.getSize() == MAX_DECK_SIZE;
    }

    private <T> List<T> chooseRandom(List<T> list, int n) {
        int k = list.size() - n;
        for (int i = 0; i < k; i++) {
            list.remove((int) (Math.random() * list.size()));

        }
        return list;
    }

    private List<PassiveOverview> turnToPassiveOverview(List<Passive> passives) {
        List<PassiveOverview> result = new ArrayList<>();
        passives.forEach(p -> result.add(new PassiveOverview(p)));
        return result;
    }

    public void selectPassive(String passiveName) {
        Optional<Passive> optionalPassive = modelLoader.getPassive(passiveName);
        if (optionalPassive.isPresent() && canStartGame()) {
            game = new Game(player.getSelectedDeck(), optionalPassive.get(), player, connector);
            connector.save(player);
            sendPlayDetails();
        }
    }

    private void sendPlayDetails() {
        List<CardOverview> hand = game.getHandCard().stream()
                .map(card -> new CardOverview(card, 1, false)).collect(Collectors.toList());
        List<CardOverview> ground = game.getGround().stream()
                .map(card -> new CardOverview(card, 1, false)).collect(Collectors.toList());
        CardOverview weapon = game.getActiveWeapon() == null ?
                null : new CardOverview(game.getActiveWeapon(), 1, false);
        HeroOverview hero = new HeroOverview(game.getHero());
        HeroPowerOverview heroPower = new HeroPowerOverview(game.getHero().getPower());
        String eventLog = game.getGameEvents();
        Response response = new PlayDetails(hand, ground, weapon, hero
                , heroPower, eventLog, game.getMana(), game.getDeck().size());
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    public void endTurn() {
        if (game != null && game.isRunning()) {
            game.endTurn();
            sendPlayDetails();
            connector.save(player);
        }
    }

    public void playCard(String cardName) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        if (optionalCard.isPresent() && game != null && game.isRunning()) {
            game.playCard(optionalCard.get());
            sendPlayDetails();
            connector.save(player);
        }
    }

    public void exitGame() {
        if (game != null) {
            game.exit();
            Response response = new GoTo("MAIN_MENU", null);
            Client.getInstance().putAnswer(response);
            connector.save(new ResponseLog(response, player.getUserName()));
            connector.save(player);
        }
    }
}