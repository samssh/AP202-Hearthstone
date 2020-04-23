package server;

import client.Answer;
import client.Client;
import controller.Loop;
import hibernate.Connector;
import model.Card;
import model.HeaderLog;
import model.ModelLoader;
import model.Player;
import view.panel.LoginPanel;

import java.util.ArrayList;
import java.util.List;

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
        Answer answer = new Answer.ShopDetails(makeSellList(),makeBuyList(),player.getCoin());
        Client.getInstance().putAnswer(answer);
    }

    private List<Card> makeSellList() {
        return new ArrayList<>(player.getCards());
    }

    private List<Card> makeBuyList() {
        List<Card> buyList = new ArrayList<>();
        for (Card card : modelLoader.getCards()) {
            int number = player.numberOfCard(card);
            if (number == 2)
                continue;
            if (number == 1) {
                if (card.getPrice() <= player.getCoin())
                    buyList.add(card);
            }
            if (number == 0) {
                if (card.getPrice() <= player.getCoin())
                    buyList.add(card);
                if (2 * card.getPrice() <= player.getCoin())
                    buyList.add(card);
            }
        }
        return buyList;
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

    void sellCard(Card card){
        if (player.getCards().contains(card)){
            player.setCoin(player.getCoin()+card.getPrice());
            player.removeCard(card);
            connector.beginTransaction();
            player.saveOrUpdate(connector);
            connector.commit();
        }
        sendShop();
    }

    void buyCard(Card card){
        if (player.getCoin()>=card.getPrice()){
            player.setCoin(player.getCoin()-card.getPrice());
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