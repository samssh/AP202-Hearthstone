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

    void login(String username, String password, LoginPanel.Mode mode) {
        if (mode == LoginPanel.Mode.SIGN_IN)
            signIn(username, password);
        if (mode == LoginPanel.Mode.SIGN_UP)
            signUp(username, password);
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
                    player.getCards().get(card).getRepeatedTimes(), 0,true));
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
                    buyList.add(new CardOverview(card, 1, 0,true));
            }
            if (number == 0) {
                if (2 * card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 2, 0,true));
                else if (card.getPrice() <= player.getCoin())
                    buyList.add(new CardOverview(card, 1, 0,true));

            }
        }
        return buyList;
    }

    void sendStatus() {
        Answer answer = new Answer.StatusDetail(makeStatusDetails());
        Client.getInstance().putAnswer(answer);
    }

    private List<DeckOverview> makeStatusDetails() {
        List<DeckOverview> result = new ArrayList<>();
        List<Deck> sortedList = new ArrayList<>(player.getDecks());
        sortedList.sort(this::compareDeck);
        for (int i = 0; i < player.getDecks().size(); i++) {
            Deck deck = sortedList.get(i);
            Card card = getMVC(deck);
            if (card!=null) result.add(new DeckOverview(deck, card.getName()));
            else result.add(new DeckOverview(deck, null));
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

    private void signIn(String userName, String password) {
        Player p = connector.fetch(Player.class, userName);
        if (p != null) {
            if (p.getPassword().equals(password)) {
                this.player = p;
                Client.getInstance().putAnswer(new Answer.LoginAnswer(true, player.getUserName()));
            } else {
                Client.getInstance().putAnswer(new Answer.LoginAnswer(false, "wrong password"));
            }
        } else {
            Client.getInstance().putAnswer(new Answer.LoginAnswer(false, "username not exist"));
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
            Client.getInstance().putAnswer(new Answer.LoginAnswer(true, player.getUserName()));
        } else {
            Client.getInstance().putAnswer(new Answer.LoginAnswer(false, "username already exist"));
        }
    }

    void sellCard(String cardName) {
        Card card = modelLoader.searchCard(cardName);
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
        Card card = modelLoader.searchCard(cardName);
        if (player.getCoin() >= card.getPrice()) {
            player.setCoin(player.getCoin() - card.getPrice());
            player.addCard(card);
            connector.beginTransaction();
            player.saveOrUpdate(connector);
            connector.commit();
        }
        sendShop();
    }

    public void shutdown() {
        executor.stop();
        executeRequests();
        connector.close();
    }
}