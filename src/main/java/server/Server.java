package server;

import client.*;
import util.Config;
import util.ConfigFactory;
import hibernate.Connector;
import model.account.*;
import model.log.*;
import model.main.*;
import util.Loop;
import util.ModelLoader;
import view.model.*;

import java.util.*;

public class Server {
    static int STARTING_MANA;
    static int MANA_PER_TURN;
    static int CARD_PER_TURN;
    static int MAX_DECK_SIZE;
    static int STARTING_PASSIVES;
    static int STARTING_HAND_CARDS;
    static int MAX_MANA;
    static int STARTING_COINS;

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
    }

    private static final Server instance = new Server();
    private final List<Request> tempRequestList, requestList;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Loop executor;
    private Player player;
    // last filter of collection
    private int mana, lockMode;
    private String name, classOfCard;
    private Game game;

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
        Player p = connector.fetch(Player.class, username);
        if (p == null) {
            p = new Player(username, password, System.currentTimeMillis(), STARTING_COINS, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            connector.save(new HeaderLog(p.getCreatTime(), p.getUserName(), p.getPassword()));
            connector.save(p);
            this.player = p;
            Answer answer = new Answer.LoginAnswer(true, player.getUserName());
            Client.getInstance().putAnswer(answer);
            connector.save(new AnswerLog(answer, player.getUserName()));
            connector.save(new AccountLog(player.getUserName(), "sign up"));
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
        for (Card card : player.getCards().keySet()) {
            result.add(new CardOverview(card,
                    player.getCards().get(card).getRepeatedTimes(), true));
        }
        return result;
    }

    private List<CardOverview> makeBuyList() {
        List<CardOverview> buyList = new ArrayList<>();
        List<Card> availableCards = availableCards();
        for (Card card : availableCards) {
            if (player.getCards().containsKey(card)) {
                int number = player.getCards().get(card).getRepeatedTimes();
                if (number == 1) {
                    if (card.getPrice() <= player.getCoin())
                        buyList.add(new CardOverview(card, 1, true));
                }
            } else {
                if (2 * card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 2, true));
                else if (card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 1, true));

            }
        }
        return buyList;
    }

    private List<Card> availableCards() {
        List<Card> result = new ArrayList<>();
        List<Card> cards = modelLoader.getCards();
        for (Card c : cards) {
            String heroName = c.getClassOfCard().getHeroName();
            if (heroName.equals("Neutral"))
                result.add(c);
            else {
                for (Hero h : player.getHeroes())
                    if (h.getName().equals(heroName)) {
                        result.add(c);
                        break;
                    }
            }
        }
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
        boolean result = canBuy(card);
        if (result) {
            player.setCoin(player.getCoin() - card.getPrice());
            player.addCard(card);
            connector.save(player);
            connector.save(new BuySellLog(player.getUserName()
                    , player.getCoin() + card.getPrice(), player.getCoin(), cardName, "buy"));
        }
        sendShop();
    }

    private boolean canBuy(Card card) {
        if (player.getCoin() >= card.getPrice()) {
            if (player.numberOfCard(card) < 2)
                for (Hero h : player.getHeroes())
                    if (isForHero(card.getClassOfCard(), h))
                        return true;
        }
        return false;
    }

    void sendStatus() {
        Answer answer = new Answer.StatusDetails(makeStatusDetails());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<BigDeckOverview> makeStatusDetails() {
        List<BigDeckOverview> result = new ArrayList<>();
        List<Deck> sortedList = new ArrayList<>(player.getDecks());
        sortedList.sort(this::compareDeck);
        for (Deck deck : sortedList) {
            Card card = getMVC(deck);
            String MVCname = card == null ? null : card.getName();
            result.add(new BigDeckOverview(deck, MVCname));
        }
        return result;
    }

    private int compareDeck(Deck d1, Deck d2) {
        if (d2.getWinRate() == d1.getWinRate())
            if (d2.getWins() == d1.getWins())
                if (d2.getManaAverage() == d1.getManaAverage())
                    return d1.getName().compareTo(d2.getName());
                else return Double.compare(d2.getManaAverage(), d1.getManaAverage());
            else return Integer.compare(d2.getWins(), d1.getWins());
        else return Double.compare(d2.getWinRate(), d1.getWinRate());
    }

    private int compareCard(Card c1, Card c2) {
        if (c2.getRarity() == c1.getRarity())
            if (c2.getManaFrz() == c1.getManaFrz())
                if (Card.getInstanceValue(c2) == Card.getInstanceValue(c1))
                    return c2.getName().compareTo(c1.getName());
                else return Double.compare(Card.getInstanceValue(c2), Card.getInstanceValue(c1));
            else return Integer.compare(c2.getManaFrz(), c1.getManaFrz());
        else return Integer.compare(c2.getRarity().getI(), c1.getRarity().getI());
    }

    private List<Card> getMVCList(Deck deck) {
        Map<Card, CardDetails> map = deck.getCards();
        int max = 0;
        for (Card card : map.keySet()) {
            if (map.get(card).getUsage() > max)
                max = map.get(card).getUsage();
        }
        List<Card> result = new ArrayList<>();
        for (Card card : map.keySet()) {
            if (map.get(card).getUsage() == max)
                result.add(card);
        }
        return result;
    }

    private Card getMVC(Deck deck) {
        List<Card> list = getMVCList(deck);
        list.sort(this::compareCard);
        return list.size() != 0 ? list.get(0) : null;
    }

    void sendFirstCollection() {
        Answer answer = new Answer.FirstCollectionDetails(makeHeroNames(), makeClassOfCardNames());
        Client.getInstance().putAnswer(answer);
        connector.save(new AnswerLog(answer, player.getUserName()));
    }

    private List<String> makeHeroNames() {
        List<String> result = new ArrayList<>();
        for (Hero h : player.getHeroes()) {
            result.add(h.getName());
        }
        return result;
    }

    private List<String> makeClassOfCardNames() {
        List<String> result = new ArrayList<>();
        result.add("All classes");
        for (ClassOfCard c : modelLoader.getClassOfCards()) {
            result.add(c.getHeroName());
        }

        return result;
    }

    void sendCollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
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
        if (name != null) result = filterName(result, name);
        if (mana != 0) result = filterMana(result, mana);
        if (classOfCard != null) result = filterClassOfCard(result, classOfCard);
        return filterLockMode(result, lockMode, deck);
    }

    private List<Card> filterName(List<Card> cards, String name) {
        List<Card> result = new ArrayList<>();
        for (Card c : cards) {
            if (c.getName().toLowerCase().contains(name))
                result.add(c);
        }
        return result;
    }

    private List<Card> filterMana(List<Card> cards, int mana) {
        List<Card> result = new ArrayList<>();
        for (Card c : cards) {
            if (c.getManaFrz() == mana)
                result.add(c);
        }
        return result;
    }

    private List<Card> filterClassOfCard(List<Card> cards, ClassOfCard classOfCard) {
        List<Card> result = new ArrayList<>();
        for (Card c : cards) {
            if (c.getClassOfCard().equals(classOfCard))
                result.add(c);
        }
        return result;
    }

    private List<CardOverview> filterLockMode(List<Card> cards, int lockMode, Deck deck) {
        List<CardOverview> result = new ArrayList<>();
        Map<Card, CardDetails> map = player.getCards();
        Map<Card, CardDetails> deckMap;
        if (deck != null) deckMap = deck.getCards();
        else deckMap = Collections.emptyMap();
        boolean locked = true, unlocked = true;
        if (lockMode == 2) locked = false;
        if (lockMode == 1) unlocked = false;
        for (Card c : cards) {
            if (map.containsKey(c)) {
                if (deckMap.containsKey(c)) {
                    int r = map.get(c).getRepeatedTimes() - deckMap.get(c).getRepeatedTimes();
                    if (r > 0)
                        if (unlocked) result.add(new CardOverview(c, r, false));
                } else if (unlocked) result.add(new CardOverview(c, map.get(c).getRepeatedTimes(), false));
            } else {
                if (locked) result.add(new CardOverview(c, 0, false));
            }
        }
        return result;
    }

    private List<SmallDeckOverview> makeDeckList() {
        List<SmallDeckOverview> result = new ArrayList<>();
        player.getDecks().forEach(d -> result.add(new SmallDeckOverview(d)));
        return result;
    }

    private List<CardOverview> getDeckCards(Deck deck) {
        if (deck == null)
            return null;
        List<CardOverview> result = new ArrayList<>();
        Map<Card, CardDetails> map = deck.getCards();
        for (Card c : map.keySet()) {
            result.add(new CardOverview(c, map.get(c).getRepeatedTimes(), false));
        }
        return result;
    }

    private Deck getDeck(String deckName) {
        List<Deck> decks = player.getDecks();
        for (Deck d : decks) if (d.getName().equals(deckName)) return d;
        return null;
    }

    private boolean canAddDeck() {
        return player.getDecks().size() <= 15;
    }

    private boolean canChangeHero(Deck deck) {
        if (deck == null)
            return false;
        for (Card c : deck.getCards().keySet()) {
            if (!c.getClassOfCard().equals(modelLoader.getClassOfCard("Neutral")))
                return false;
        }
        return true;
    }

    void newDeck(String deckName, String heroName) {
        Answer answer;
        if (!containDeckName(deckName) && containHero(heroName)) {
            Hero h = modelLoader.getHero(heroName);
            player.getDecks().add(new Deck(h, deckName));
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);
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
        Hero h = modelLoader.getHero(heroName);
        return player.getHeroes().contains(h);
    }

    private boolean containDeckName(String deckName) {
        boolean result = false;
        for (Deck d : player.getDecks()) {
            if (d.getName().equals(deckName)) {
                result = true;
                break;
            }
        }
        return result;
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
        List<CardOverview> hand = new ArrayList<>();
        game.getHandCard().forEach(card -> hand.add(new CardOverview(card, 1, false)));
        List<CardOverview> ground = new ArrayList<>();
        game.getGround().forEach(card -> ground.add(new CardOverview(card, 1, false)));
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