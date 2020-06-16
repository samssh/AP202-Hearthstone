package ir.SAM.hearthstone.server;

import ir.SAM.hearthstone.Transmitters.ResponseSender;
import ir.SAM.hearthstone.requests.Request;
import ir.SAM.hearthstone.hibernate.Connector;
import ir.SAM.hearthstone.model.account.Deck;
import ir.SAM.hearthstone.model.account.Player;
import ir.SAM.hearthstone.model.log.*;
import ir.SAM.hearthstone.model.main.*;
import ir.SAM.hearthstone.resourceManager.Config;
import ir.SAM.hearthstone.resourceManager.ConfigFactory;
import ir.SAM.hearthstone.response.*;
import ir.SAM.hearthstone.util.Loop;
import ir.SAM.hearthstone.resourceManager.ModelLoader;
import ir.SAM.hearthstone.view.model.*;

import java.util.*;
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

    private final List<Request> tempRequestList, requestList;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Loop executor;
    private Player player;
    private Game game;
    private final Collection collection;
    private final Shop shop;
    private final Status status;
    private final ResponseSender responseSender;

    public Server(ResponseSender responseSender) {
        this.responseSender = responseSender;
        tempRequestList = new LinkedList<>();
        requestList = new LinkedList<>();
        connector = new Connector(ConfigFactory.getInstance().getConfigFile("SERVER_HIBERNATE_CONFIG")
                , System.getenv("HearthStone password"));
        modelLoader = new ModelLoader(connector);
        collection = new Collection(connector, modelLoader);
        shop = new Shop(connector, modelLoader);
        status = new Status();
        executor = new Loop(60, this::executeRequests);
        executor.start();
    }

    private void executeRequests() {
        synchronized (tempRequestList) {
            requestList.addAll(tempRequestList);
            tempRequestList.clear();
        }
        requestList.forEach(request -> request.execute(this));
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
        connector.close();
    }

    private void sendResponse(Response... responses) {
        for (Response response : responses)
            if (response != null) {
                responseSender.sendResponse(response);
                connector.save(new ResponseLog(response, player.getUserName()));
            }
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
                sendResponse(response);
//                connector.save(new ResponseLog(ir.SAM.hearthstone.response, player.getUserName()));
                connector.save(new AccountLog(player.getUserName(), "sign in"));
            } else {
                Response response = new LoginResponse(false, "wrong password");
                responseSender.sendResponse(response);
                connector.save(new ResponseLog(response, p.getUserName()));
            }
        } else {
            Response response = new LoginResponse(false, "username not exist");
            responseSender.sendResponse(response);
            connector.save(new ResponseLog(response, null));
        }
    }

    private void signUp(String username, String password) {
        Player player = connector.fetch(Player.class, username);
        if (player == null) {
            player = new Player(username, password, System.currentTimeMillis(), STARTING_COINS, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            connector.save(new HeaderLog(player.getCreatTime(), player.getUserName(), player.getPassword()));
            connector.save(player);
            this.player = player;
            Response response = new LoginResponse(true, this.player.getUserName());
            sendResponse(response);
//            connector.save(new ResponseLog(ir.SAM.hearthstone.response, this.player.getUserName()));
            connector.save(new AccountLog(this.player.getUserName(), "sign up"));
        } else {
            Response response = new LoginResponse(false, "username already exist");
            responseSender.sendResponse(response);
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
            HeaderLog header = connector.fetch(HeaderLog.class, player.getCreatTime());
            header.setDeletedAt(System.currentTimeMillis() + "");
            connector.save(header);
            this.player = null;
        }
    }

    public void sendShop() {
        sendResponse(shop.sendShop(player));
    }

    public void sellCard(String cardName) {
        sendResponse(shop.sellCard(cardName, player));
    }

    public void buyCard(String cardName) {
        sendResponse(shop.buyCard(cardName, player));
    }

    public void sendStatus() {
        sendResponse(status.sendStatus(player));
    }

    public void sendFirstCollection() {
        sendResponse(collection.sendFirstCollection(player));
    }

    public void sendCollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        sendResponse(collection.sendCollectionDetails(name, classOfCard, mana, lockMode, deckName, player));
    }

    public void newDeck(String deckName, String heroName) {
        sendResponse(collection.newDeck(deckName, heroName, player));
    }

    public void deleteDeck(String deckName) {
        sendResponse(collection.deleteDeck(deckName, player));
    }

    public void changeDeckName(String oldDeckName, String newDeckName) {
        sendResponse(collection.changeDeckName(oldDeckName, newDeckName, player));
    }

    public void changeHeroDeck(String deckName, String heroName) {
        sendResponse(collection.changeHeroDeck(deckName, heroName, player));
    }

    public void removeCardFromDeck(String cardName, String deckName) {
        sendResponse(collection.removeCardFromDeck(cardName, deckName, player));
    }

    public void addCardToDeck(String cardName, String deckName) {
        sendResponse(collection.addCardToDeck(cardName, deckName, player, shop));
    }

    public void startPlay() {
        Response response;
        if (canStartGame()) {
            List<Passive> passives = chooseRandom(modelLoader.getFirstPassives(), STARTING_PASSIVES);
            response = new PassiveDetails(turnToPassiveOverview(passives));
        } else {
            response = new GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        sendResponse(response);
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
        sendResponse(response);
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
            sendResponse(response);
            connector.save(player);
        }
    }
}