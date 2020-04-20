package server;

import client.Answer;
import client.Client;
import controller.Loop;
import hibernate.Connector;
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
        requestList.forEach(Request::execute);
        requestList.clear();
        synchronized (tempRequestList) {
            requestList.addAll(tempRequestList);
            tempRequestList.clear();
        }
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

    private void signIn(String userName, String password) {
        Player p = connector.fetch(Player.class, userName);
        if (p != null) {
            if (p.getPassword().equals(password)) {
                Client.getInstance().putAnswer(new Answer.LoginAnswer(p,"successful"));
            } else {
                Client.getInstance().putAnswer(new Answer.LoginAnswer(null,"wrong password"));
            }
        } else {
            Client.getInstance().putAnswer(new Answer.LoginAnswer(null,"username not exist"));
        }
    }

    private void signUp(String username, String password) {
        Player p = connector.fetch(Player.class, username);
        if (p == null) {
            connector.beginTransaction();
            p = new Player(username, password, System.currentTimeMillis(), 30, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            HeaderLog headerLog = new HeaderLog(p.getCreatTime(), p.getUserName(), p.getPassword());
            headerLog.saveOrUpdate(connector);
            connector.commit();
            Client.getInstance().putAnswer(new Answer.LoginAnswer(p,"successful"));
        } else {
            Client.getInstance().putAnswer(new Answer.LoginAnswer(null,"username already exist"));
        }
    }
}