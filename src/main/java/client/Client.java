package client;

import util.Executable;
import util.Loop;
import hibernate.Connector;
import view.model.CardOverview;
import server.Request;
import server.Server;
import view.MyFrame;
import view.model.DeckOverview;
import view.panel.LoginPanel;
import view.panel.MainMenuPanel;
import view.PanelType;
import view.panel.ShopPanel;
import view.panel.StatusPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class Client {
    private static final Client instance = new Client();
    private final JFrame frame;
    private final Map<PanelType, JPanel> panels;
    private final Stack<PanelType> history;
    private PanelType now;
    private Connector connector;
    private final List<Executable> tempAnswerList, answerList;
    private final Loop executor;
    private String username;

    public Client() {
        this.frame = new MyFrame();
        panels = new HashMap<>();
        history = new Stack<>();
        panels.put(PanelType.LOGIN_PANEL, new LoginPanel(new LoginPanelAction()));
        panels.put(PanelType.MAIN_MENU, new MainMenuPanel(new MainMenuAction()));
        panels.put(PanelType.SHOP, new ShopPanel(new ShopAction()));
        panels.put(PanelType.STATUS,new StatusPanel(new StatusAction()));
        now = PanelType.LOGIN_PANEL;
        updateFramePanel();
        tempAnswerList = new ArrayList<>();
        answerList = new ArrayList<>();
        executor = new Loop(60, this::executeAnswers);
        executor.start();

    }

    public static Client getInstance() {
        return instance;
    }

    private void executeAnswers() {
        answerList.forEach(Executable::execute);
        answerList.clear();
        synchronized (tempAnswerList) {
            answerList.addAll(tempAnswerList);
            tempAnswerList.clear();
        }
    }

    public void putAnswer(Answer answer) {
        if (answer != null)
            synchronized (tempAnswerList) {
                tempAnswerList.add(answer);
            }
    }

    void login(boolean success, String message) {
        LoginPanel panel = (LoginPanel) panels.get(PanelType.LOGIN_PANEL);
        if (success) {
            panel.reset();
            now = PanelType.MAIN_MENU;
            updateFramePanel();
            ((MainMenuPanel) panels.get(now)).setPlayerName(message);
            username = message;
        } else {
            panel.setMessage(message);
        }
    }

    void setShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coin) {
        if (now != PanelType.SHOP) {
            now = PanelType.SHOP;
            updateFramePanel();
        }
        ShopPanel shopPanel = (ShopPanel) panels.get(PanelType.SHOP);
        shopPanel.setSell(sell);
        shopPanel.setBuy(buy);
        shopPanel.setCoin(coin);
    }

    void setStatusDetails(List<DeckOverview> deckOverviews){
        if (now != PanelType.STATUS) {
            now = PanelType.STATUS;
            updateFramePanel();
        }
        StatusPanel statusPanel = (StatusPanel) panels.get(PanelType.STATUS);
        statusPanel.setDeckBoxList(deckOverviews);
    }

    private void updateFramePanel() {
        frame.setContentPane(panels.get(now));
        history.push(now);
    }

    private void back() {
        history.pop();
        now = history.peek();
        frame.setContentPane(panels.get(now));
    }

    private void backMainMenu() {
        while (history.peek() != PanelType.MAIN_MENU)
            history.pop();
        now = history.peek();
        frame.setContentPane(panels.get(now));
    }

    private void exit() {
        Server.getInstance().shutdown();
        this.shutdown();
        System.exit(0);
    }

    private void logout() {
        Request request = new Request.LogoutRequest();
        Server.getInstance().addRequest(request);
    }

    private void deleteAccount() {
        Request request = new Request.DeleteAccount();
        Server.getInstance().addRequest(request);
    }

    private void shutdown() {
        // connector
        executor.stop();
    }

    public class LoginPanelAction {
        public void changeMode(LoginPanel loginPanel) {
            if (loginPanel.getMode() == LoginPanel.Mode.SIGN_IN) {
                loginPanel.setMode(LoginPanel.Mode.SIGN_UP);
            } else {
                loginPanel.setMode(LoginPanel.Mode.SIGN_IN);
            }
            loginPanel.reset();
        }

        public void login(LoginPanel loginPanel, String username, String pass, String pass2) {
            if ("Enter username".equals(username) || "".equals(username))
                return;
            if (loginPanel.getMode() == LoginPanel.Mode.SIGN_UP && !pass.equals(pass2)) {
                loginPanel.setMessage("password not same");
                return;
            }
            if ("Enter password".equals(pass))
                return;
            Request request = new Request.LoginRequest(username, pass, loginPanel.getMode());
            Server.getInstance().addRequest(request);
        }

        public void exit() {
            Client.this.exit();
        }
    }

    public class MainMenuAction {
        public void exit(ActionEvent e) {
            Client.this.logout();
            Client.this.exit();
        }

        public void logout(ActionEvent e) {
            Client.this.logout();
            // reset panels
            now = PanelType.LOGIN_PANEL;
            updateFramePanel();
        }

        public void deleteAccount(ActionEvent e) {
            Client.this.deleteAccount();
            // reset panels
            now = PanelType.LOGIN_PANEL;
            updateFramePanel();
        }

        public void shop(ActionEvent e) {
            now = PanelType.SHOP;
            Request request = new Request.Shop();
            Server.getInstance().addRequest(request);
            updateFramePanel();
        }

        public void Status(ActionEvent e) {
            now = PanelType.STATUS;
            Request request = new Request.Status();
            Server.getInstance().addRequest(request);
            updateFramePanel();
        }
    }

    public class ShopAction {
        public void sell(String cardName) {
            Request request = new Request.SellCard(cardName);
            Server.getInstance().addRequest(request);
        }

        public void buy(String cardName) {
            Request request = new Request.BuyCard(cardName);
            Server.getInstance().addRequest(request);
        }

        public void exit(ActionEvent e) {
            Client.this.logout();
            Client.this.exit();
        }

        public void back(ActionEvent e) {
            Client.this.back();
        }

        public void backMainMenu(ActionEvent e) {
            Client.this.backMainMenu();
        }
    }

    public class StatusAction {
        public void exit(ActionEvent e) {
            Client.this.logout();
            Client.this.exit();
        }

        public void back(ActionEvent e) {
            Client.this.back();
        }

        public void backMainMenu(ActionEvent e) {
            Client.this.backMainMenu();
        }
    }
}
