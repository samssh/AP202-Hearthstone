package server;

import client.Answer;
import client.Client;
import util.Loop;
import util.ModelLoader;
import hibernate.Connector;
import model.*;
import view.model.CardOverview;
import view.model.DeckOverview;
import view.panel.LoginPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
    private static final Server instance = new Server();
    private final List<Request> tempRequestList, requestList;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Loop executor;
    private Player player;

    public static Server getInstance() {
        return instance;
    }

    private Server() {
        tempRequestList = new ArrayList<>();
        requestList = new ArrayList<>();
        connector = new Connector("HIBERNATE_CONFIG");
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
        if (request != null)
            synchronized (tempRequestList) {
                tempRequestList.add(request);
            }
    }

    public void shutdown() {
        executor.stop();
        executeRequests();
        connector.close();
    }

    void login(String username, String password, LoginPanel.Mode mode) {
        if (mode == LoginPanel.Mode.SIGN_IN)
            signIn(username, password);
        if (mode == LoginPanel.Mode.SIGN_UP)
            signUp(username, password);
    }


    private void signIn(String userName, String password) {
        Player p = connector.fetch(Player.class, userName);
        if (p != null) {
            if (p.getPassword().equals(password)) {
                this.player = p;
                Answer answer = new Answer.LoginAnswer(true, player.getUserName());
                Client.getInstance().putAnswer(answer);
            } else {
                Answer answer = new Answer.LoginAnswer(false, "wrong password");
                Client.getInstance().putAnswer(answer);
            }
        } else {
            Answer answer = new Answer.LoginAnswer(false, "username not exist");
            Client.getInstance().putAnswer(answer);
        }
    }

    private void signUp(String username, String password) {
        Player p = connector.fetch(Player.class, username);
        if (p == null) {
            connector.beginTransaction();
            p = new Player(username, password, System.currentTimeMillis(), 30, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            HeaderLog headerLog = new HeaderLog(p.getCreatTime(), p.getUserName(), p.getPassword());
            p.saveOrUpdate(connector);
            headerLog.saveOrUpdate(connector);
            connector.commit();
            this.player = p;
            Answer answer = new Answer.LoginAnswer(true, player.getUserName());
            Client.getInstance().putAnswer(answer);
        } else {
            Answer answer = new Answer.LoginAnswer(false, "username already exist");
            Client.getInstance().putAnswer(answer);
        }
    }

    void logout() {
        if (this.player != null) {
            connector.beginTransaction();
            this.player.saveOrUpdate(connector);
            connector.commit();
            // log
            this.player = null;
        }
    }

    void deleteAccount() {
        if (this.player != null) {
            connector.beginTransaction();
            this.player.delete(connector);
            connector.commit();
            // log
            this.player = null;
        }
    }

    void sendShop() {
        Answer answer = new Answer.ShopDetails(makeSellList(), makeBuyList(), player.getCoin());
        Client.getInstance().putAnswer(answer);
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
        for (Card card : modelLoader.getCards()) {
            int number = player.numberOfCard(card);
            if (number == 2)
                continue;
            if (number == 1) {
                if (card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 1, true));
            }
            if (number == 0) {
                if (2 * card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 2, true));
                else if (card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 1, true));

            }
        }
        return buyList;
    }

    void sellCard(String cardName) {
        Card card = modelLoader.getCard(cardName);
        if (player.getCards().containsKey(card)) {
            player.setCoin(player.getCoin() + card.getPrice());
            player.removeCard(card);
            connector.beginTransaction();
            player.saveOrUpdate(connector);
            connector.commit();
        }
        sendShop();
    }

    void buyCard(String cardName) {
        Card card = modelLoader.getCard(cardName);
        if (player.getCoin() >= card.getPrice()) {
            player.setCoin(player.getCoin() - card.getPrice());
            player.addCard(card);
            connector.beginTransaction();
            player.saveOrUpdate(connector);
            connector.commit();
        }
        sendShop();
    }

    void sendStatus() {
        Answer answer = new Answer.StatusDetails(makeStatusDetails());
        Client.getInstance().putAnswer(answer);
    }

    private List<DeckOverview> makeStatusDetails() {
        List<Deck> sortedList = new ArrayList<>(player.getDecks());
        sortedList.sort(this::compareDeck);
        return turnToDeckOverview(sortedList,true);
    }

    private List<DeckOverview> turnToDeckOverview(List<Deck> decks, boolean hasMVC) {
        List<DeckOverview> result = new ArrayList<>();
        for (Deck deck : decks) {
            if (hasMVC) {
                Card card = getMVC(deck);
                if (card != null) result.add(new DeckOverview(deck, card.getName()));
                else result.add(new DeckOverview(deck, null));
            } else result.add(new DeckOverview(deck, null));
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
        Answer answer = new Answer.FirstCollectionDetails(makeHeroNames());
        Client.getInstance().putAnswer(answer);
    }

    private List<String> makeHeroNames() {
        List<String> result = new ArrayList<>();
        for (Hero h : player.getHeroes()) {
            result.add(h.getName());
        }
        return result;
    }

    void sendCollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard), mana, lockMode);
        List<DeckOverview> decks = makeDeckList();
        Deck d=getDeck(deckName);
        List<CardOverview> deckCards = getDeckCards(d);
        boolean canAddDeck = canAddDeck();
        boolean canChangeHero = canChangeHero(d);
        Answer answer = new Answer.CollectionDetails(cards, decks,deckCards,canAddDeck,canChangeHero);
        Client.getInstance().putAnswer(answer);
    }

    private List<CardOverview> makeCardsList(String name, ClassOfCard classOfCard, int mana, int lockMode) {
        List<Card> result = new ArrayList<>(modelLoader.getCards());
        if (name != null) result = filterName(result, name);
        if (mana != 0) result = filterMana(result, mana);
        if (classOfCard != null) result = filterClassOfCard(result, classOfCard);
        return filterLockMode(result, lockMode);
    }

    private List<Card> filterName(List<Card> cards, String name) {
        List<Card> result = new ArrayList<>();
        for (Card c : cards) {
            if (c.getName().contains(name))
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

    private List<CardOverview> filterLockMode(List<Card> cards, int lockMode) {
        List<CardOverview> result = new ArrayList<>();
        Map<Card, CardDetails> map = player.getCards();
        boolean locked = true, unlocked = true;
        if (lockMode == 1) locked = false;
        if (lockMode == 2) unlocked = false;
        for (Card c : cards) {
            if (map.containsKey(c)) {
                if (unlocked) result.add(new CardOverview(c, map.get(c).getRepeatedTimes(), false));
            } else {
                if (locked) result.add(new CardOverview(c, 0, false));
            }
        }
        return result;
    }

    private List<DeckOverview> makeDeckList(){
        return turnToDeckOverview(player.getDecks(),false);
    }

    private List<CardOverview> getDeckCards(Deck deck){
        if (deck==null)
            return null;
        List<CardOverview> result = new ArrayList<>();
        Map<Card,CardDetails> map = deck.getCards();
        for (Card c :map.keySet()) {
            result.add(new CardOverview(c,map.get(c).getRepeatedTimes(),false));
        }
        return result;
    }

    private Deck getDeck(String deckName){
        List<Deck> decks = player.getDecks();
        for (Deck d:decks) if (d.getName().equals(deckName)) return d;
        return null;
    }

    private boolean canAddDeck(){
        return player.getDecks().size()<=15;
    }

    private boolean canChangeHero(Deck deck){
        if (deck==null)
            return false;
        for (Card c:deck.getCards().keySet()) {
            if(!c.getClassOfCard().equals(modelLoader.getClassOfCard("Neutral")))
                return false;
        }
        return true;
    }
}