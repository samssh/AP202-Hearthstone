package server;

import client.Answer;
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
import java.util.stream.Stream;

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
    // last filter of collection
    private int mana, lockMode;
    private String name, classOfCard;
    private String deckName;

    public static Server getInstance() {
        return instance;
    }

    private Server() {
        tempRequestList = new ArrayList<>();
        requestList = new ArrayList<>();
        connector = new Connector("SERVER_HIBERNATE_CONFIG");
        modelLoader = new ModelLoader(connector);
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

    void login(String username, String password, int mode) {
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
                Answer answer = new Answer.LoginAnswer(true, player.getUserName());
                Client.getInstance().putAnswer(answer);
                connector.save(new AnswerLog(answer, player.getUserName()));
                connector.save(new AccountLog(player.getUserName(), "sign in"));
            } else {
                Answer answer = new Answer.LoginAnswer(false, "wrong password");
                Client.getInstance().putAnswer(answer);
                connector.save(new AnswerLog(answer, p.getUserName()));
            }
        } else {
            Answer answer = new Answer.LoginAnswer(false, "username not exist");
            Client.getInstance().putAnswer(answer);
            connector.save(new AnswerLog(answer, null));
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
            Answer answer = new Answer.LoginAnswer(true, this.player.getUserName());
            Client.getInstance().putAnswer(answer);
            connector.save(new AnswerLog(answer, this.player.getUserName()));
            connector.save(new AccountLog(this.player.getUserName(), "sign up"));
        } else {
            Answer answer = new Answer.LoginAnswer(false, "username already exist");
            Client.getInstance().putAnswer(answer);
            connector.save(new AnswerLog(answer, null));
        }
    }

    void logout() {
        if (this.player != null) {
            connector.save(player);
            connector.save(new AccountLog(player.getUserName(), "logout"));
            this.player = null;
        }
    }

    void deleteAccount() {
        if (this.player != null) {
            connector.delete(player);
            connector.save(new AccountLog(player.getUserName(), "delete account"));
            HeaderLog h = connector.fetch(HeaderLog.class, player.getCreatTime());
            h.setDeletedAt(System.currentTimeMillis() + "");
            connector.save(h);
            this.player = null;
        }
    }

    void sendShop() {
        Answer answer = new Answer.ShopDetails(makeSellList(), makeBuyList(), player.getCoin());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<CardOverview> makeSellList() {
        List<CardOverview> result = new ArrayList<>();
        player.getCards().keySet().forEach(card -> result.add(new CardOverview(card,
                player.getCards().get(card).getRepeatedTimes(), true)));
        return result;
    }

    private List<CardOverview> makeBuyList() {
        List<CardOverview> buyList = new ArrayList<>();
        List<Card> availableCards = availableCards();
        availableCards.stream().filter(player.getCards()::containsKey)
                .filter(c -> player.getCards().get(c).getRepeatedTimes() == 1)
                .filter(c -> c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 1, true)));
        availableCards.stream().filter(c -> !player.getCards().containsKey(c))
                .filter(c -> 2 * c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 2, true)));
        availableCards.stream().filter(c -> !player.getCards().containsKey(c))
                .filter(c -> 2 * c.getPrice() > player.getCoin())
                .filter(c -> c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 1, true)));
        return buyList;
    }

    private List<Card> availableCards() {
        List<Card> result = new ArrayList<>();
        List<Card> cards = modelLoader.getCards();
        cards.stream().filter(card -> card.getClassOfCard().getHeroName().equals("Neutral")
                || containHero(card.getClassOfCard().getHeroName())).forEach(result::add);
        return result;
    }

    void sellCard(String cardName) {
        Card card = modelLoader.getCard(cardName);
        boolean result = canSell(card);
        if (result) {
            player.setCoin(player.getCoin() + card.getPrice());
            player.removeCard(card);
            connector.save(player);
            connector.save(new BuySellLog(player.getUserName()
                    , player.getCoin() - card.getPrice(), player.getCoin(), cardName, "sell"));
        }
        sendShop();
    }

    private boolean canSell(Card card) {
        return player.getCards().containsKey(card);
    }

    void buyCard(String cardName) {
        Card card = modelLoader.getCard(cardName);
        if (canBuy(card)) {
            player.setCoin(player.getCoin() - card.getPrice());
            player.addCard(card);
            connector.save(player);
            connector.save(new BuySellLog(player.getUserName()
                    , player.getCoin() + card.getPrice(), player.getCoin(), cardName, "buy"));
        }
        sendShop();
    }

    private boolean canBuy(Card card) {
        return availableCards().contains(card) && player.getCoin() >= card.getPrice() && player.numberOfCard(card) < 2;
    }

    void sendStatus() {
        Answer answer = new Answer.StatusDetails(makeStatusDetails());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<BigDeckOverview> makeStatusDetails() {
        List<BigDeckOverview> result = new ArrayList<>();
        player.getDecks().stream().sorted(Comparator.comparing(Deck::getWinRate)
                .thenComparing(Deck::getWins).thenComparing(Deck::getManaAverage).thenComparing(Deck::getName))
                .forEach(deck -> result.add(createBigDeckOverview(deck)));
        return result;
    }

    private BigDeckOverview createBigDeckOverview(Deck deck) {
        Card c;
        return new BigDeckOverview(deck, (c = getMVC(deck)) == null ? null : c.getName());
    }

    private Card getMVC(Deck deck) {
        Map<Card, CardDetails> map = deck.getCards();
        List<Card> result = new ArrayList<>();
        AtomicInteger max = new AtomicInteger();
        map.values().stream().max(Comparator.comparing(CardDetails::getRepeatedTimes))
                .ifPresent(cardDetails -> max.set(cardDetails.getRepeatedTimes()));
        map.keySet().stream().filter(c -> map.get(c).getUsage() == max.get()).forEach(result::add);
        return result.stream().max(Comparator.comparing(Card::getRarity).thenComparing(Card::getManaFrz)
                .thenComparing(Card::getInstanceValue).thenComparing(Card::getName))
                .orElse(null);
    }

    void sendFirstCollection() {
        Answer answer = new Answer.FirstCollectionDetails(makeHeroNames(), makeClassOfCardNames());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<String> makeHeroNames() {
        return player.getHeroes().stream().map(Hero::getName).collect(Collectors.toList());
    }

    private List<String> makeClassOfCardNames() {
        return modelLoader.getClassOfCards().stream().map(ClassOfCard::getHeroName).collect(Collectors.toList());
    }

    void sendCollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
        this.deckName = deckName;
        List<SmallDeckOverview> decks = makeDeckList();
        Deck d = getDeck(deckName);
        if (d != null) {
            if (player.getSelectedDeckIndex() != player.getDecks().indexOf(d)) {
                player.setSelectedDeckIndex(player.getDecks().indexOf(d));
                connector.save(player);
                connector.save(new CollectionLog(player.getUserName(), null, null, deckName,
                        "selected deck", null));
            }
        }
        List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard), mana, lockMode, d);
        List<CardOverview> deckCards = getDeckCards(d);
        boolean canAddDeck = canAddDeck();
        boolean canChangeHero = canChangeHero(d);
        Answer answer = new Answer.CollectionDetails(cards, decks, deckCards, canAddDeck, canChangeHero, deckName);
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<CardOverview> makeCardsList(String name, ClassOfCard classOfCard, int mana, int lockMode, Deck deck) {
        List<Card> result = new ArrayList<>(modelLoader.getCards());
        Stream<Card> cardStream = result.stream();
        if (name != null) cardStream = cardStream.filter(c -> c.getName().toLowerCase().contains(name));
        if (mana != 0) cardStream = cardStream.filter(c -> c.getManaFrz() == mana);
        if (classOfCard != null) cardStream = cardStream.filter(c -> c.getClassOfCard().equals(classOfCard));
        return cardStream.map(c -> filterLockMode(c, lockMode, deck))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private CardOverview filterLockMode(Card card, int lockMode, Deck deck) {
        CardOverview result = null;
        Map<Card, CardDetails> map = player.getCards();
        Map<Card, CardDetails> deckMap = deck != null ? deck.getCards() : Collections.emptyMap();
        boolean locked = lockMode != 2, unlocked = lockMode != 1;
        if (map.containsKey(card)) {
            if (deckMap.containsKey(card)) {
                int r = map.get(card).getRepeatedTimes() - deckMap.get(card).getRepeatedTimes();
                if (r > 0)
                    if (unlocked) result = new CardOverview(card, r, false);
            } else if (unlocked) result = new CardOverview(card, map.get(card).getRepeatedTimes(), false);
        } else {
            if (locked) result = new CardOverview(card, 0, false);
        }
        return result;
    }

    private List<SmallDeckOverview> makeDeckList() {
        return player.getDecks().stream().map(SmallDeckOverview::new).collect(Collectors.toList());
    }

    private List<CardOverview> getDeckCards(Deck deck) {
        if (deck == null)
            return null;
        return deck.getCards().keySet().stream().
                map(c -> new CardOverview(c, deck.getCards().get(c).getRepeatedTimes(), false))
                .collect(Collectors.toList());
    }

    private Deck getDeck(String deckName) {
        return player.getDecks().stream().filter(d -> d.getName().equals(deckName)).findAny().orElse(null);
    }

    private boolean canAddDeck() {
        return player.getDecks().size() <= MAX_DECK_NUMBER;
    }

    private boolean canChangeHero(Deck deck) {
        if (deck == null)
            return false;
        return deck.getCards().keySet().stream()
                .anyMatch(c -> !c.getClassOfCard().equals(modelLoader.getClassOfCard("Neutral")));
    }

    void newDeck(String deckName, String heroName) {
        Answer answer;
        if (!containDeckName(deckName) && containHero(heroName)) {
            Hero h = modelLoader.getHero(heroName);
            player.getDecks().add(new Deck(h, deckName));
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, this.deckName);
            connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName,
                    "create deck", null));
            answer = new Answer.showMessage("deck created");
        } else {
            answer = new Answer.showMessage("deck not created");
        }
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private boolean containHero(String heroName) {
        return player.getHeroes().contains(modelLoader.getHero(heroName));
    }

    private boolean containDeckName(String deckName) {
        return player.getDecks().stream().anyMatch(d -> d.getName().equals(deckName));
    }

    void deleteDeck(String deckName) {
        Deck deck = getDeck(deckName);
        Answer answer;
        if (deck != null) {
            player.getDecks().remove(deck);
            connector.save(player);
            connector.delete(deck);
            sendCollectionDetails(name, classOfCard, mana, lockMode, null);
            answer = new Answer.showMessage("deck deleted");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    deckName, "delete deck", null));
        } else answer = new Answer.showMessage("deck not deleted");
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    void changeDeckName(String oldDeckName, String newDeckName) {
        Answer answer;
        if (containDeckName(oldDeckName) && !containDeckName(newDeckName)) {
            Deck deck = getDeck(oldDeckName);
            Objects.requireNonNull(deck).setName(newDeckName);
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, newDeckName);
            answer = new Answer.showMessage("deck name changed");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    oldDeckName, "change deck name", newDeckName));
        } else answer = new Answer.showMessage("deck name not changed");
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    void changeHeroDeck(String deckName, String heroName) {
        Answer answer;
        if (containDeckName(deckName) && containHero(heroName)) {
            Hero h = modelLoader.getHero(heroName);
            Deck deck = getDeck(deckName);
            Objects.requireNonNull(deck).setHero(h);
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);
            connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName
                    , "change hero", null));
            answer = new Answer.showMessage("hero deck changed");
        } else {
            answer = new Answer.showMessage("hero deck not changed");
        }
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    void removeCardFromDeck(String cardName, String deckName) {
        Card card = modelLoader.getCard(cardName);
        Deck deck = getDeck(deckName);
        if (card != null && deck != null) {
            deck.removeCard(card);
            sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);
            connector.save(player);
            connector.save(new CollectionLog(player.getUserName(), cardName, null
                    , deckName, "remove Card", null));
        }
    }

    void addCardToDeck(String cardName, String deckName) {
        Card card = modelLoader.getCard(cardName);
        Answer answer = null;
        if (card != null) {
            Map<Card, CardDetails> playerCards = player.getCards();
            if (playerCards.containsKey(card)) {
                Deck deck = getDeck(deckName);
                if (deck != null) {
                    if (playerCards.get(card).getRepeatedTimes() > deck.numberOfCard(card)) {
                        if (isForHero(card.getClassOfCard(), deck.getHero())) {
                            if (deck.getSize() < MAX_DECK_SIZE) {
                                deck.addCard(card);
                                connector.save(player);
                                connector.save(new CollectionLog(player.getUserName(), cardName, null
                                        , deckName, "add Card", null));
                                sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);

                            } else {
                                answer = new Answer.showMessage("cant add card to deck\ndeck is full!!!");
                            }
                        } else {
                            answer = new Answer.showMessage("cant add card to deck\nchange hero of deck");
                        }
                    } else answer = new Answer.showMessage("cant add card to deck");
                }
            } else {
                if (canBuy(card)) {
                    answer = new Answer.GoTo("SHOP", "you can buy this card in shop\ngoto shop?");
                } else {
                    answer = new Answer.showMessage("you dont have this card\nyou cant buy card");
                }
            }
        } else {
            answer = new Answer.showMessage("cant add card to deck");
        }
        Client.getInstance().putAnswer(answer);
        if (answer != null)
            connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private boolean isForHero(ClassOfCard classOfCard, Hero hero) {
        if (classOfCard.getHeroName().equals(hero.getName()))
            return true;
        return classOfCard.equals(modelLoader.getClassOfCard("Neutral"));
    }

    void startPlay() {
        Answer answer;
        if (canStartGame()) {
            List<Passive> passives = chooseRandom(modelLoader.getFirstPassives(), STARTING_PASSIVES);
            answer = new Answer.PassiveDetails(turnToPassiveOverview(passives));
        } else {
            answer = new Answer.GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));

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

    void selectPassive(String passiveName) {
        Passive p = modelLoader.getPassive(passiveName);
        if (p != null && canStartGame()) {
            game = new Game(player.getSelectedDeck(), p, player, connector);
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
        Answer answer = new Answer.PlayDetails(hand, ground, weapon, hero
                , heroPower, eventLog, game.getMana(), game.getDeck().size());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    void endTurn() {
        if (game != null && game.isRunning()) {
            game.endTurn();
            sendPlayDetails();
            connector.save(player);
        }
    }

    void playCard(String cardName) {
        Card card = modelLoader.getCard(cardName);
        if (card != null && game != null && game.isRunning()) {
            game.playCard(card);
            sendPlayDetails();
            connector.save(player);
        }
    }

    void exitGame() {
        if (game != null) {
            game.exit();
            Answer answer = new Answer.GoTo("MAIN_MENU", null);
            Client.getInstance().putAnswer(answer);
            connector.save(new AnswerLog(answer, player.getUserName()));
            connector.save(player);
        }
    }
}