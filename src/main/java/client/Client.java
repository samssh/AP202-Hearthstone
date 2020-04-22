package client;

import controller.Executable;
import controller.Loop;
import hibernate.Connector;
import model.Player;
import server.Request;
import server.Server;
import view.MyFrame;
import view.panel.LoginPanel;
import view.panel.MainMenuPanel;
import view.panel.PanelType;

import javax.swing.*;
import java.util.*;

public class Client {
    private static final Client instance = new Client();
    private final JFrame frame;
    private final Map<PanelType, JPanel> panels;
//    private List<PanelType> history;
    private PanelType now;
    private Connector connector;
    private final List<Executable> tempAnswerList, answerList;
    private final Loop executor;
    private Player player;

    public Client() {
        this.frame = new MyFrame();
        panels = new HashMap<>();
//        history = new LinkedList<>();
        panels.put(PanelType.LOGIN_PANEL, new LoginPanel(new LoginPanelAction()));
        panels.put(PanelType.MAIN_MENU, new MainMenuPanel(new MainMenuAction()));
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

    void login(Player player, String message) {
        LoginPanel panel = (LoginPanel) panels.get(PanelType.LOGIN_PANEL);
        if (player == null) {
            panel.setMessage(message);
        } else {
            this.player = player;
            panel.reset();
            now = PanelType.MAIN_MENU;
            updateFramePanel();
            ((MainMenuPanel) panels.get(now)).setPlayerName(player.getUserName());
        }
    }

    private void updateFramePanel(){
        frame.setContentPane(panels.get(now));
//        history.add(now);
    }

//    private void back(){
//        int lastIndex = history.size() - 1;
//        now = history.get(lastIndex);
//        frame.setContentPane(panels.get(now));
//        history.remove(history.size()-1);
//    }


    private void exit(){
        Server.getInstance().shutdown();
        this.shutdown();
        System.exit(0);
    }

    private void logout(){
        Request request = new Request.LogoutRequest(player);
        Server.getInstance().addRequest(request);
        player = null;
    }

    private void deleteAccount(){
        Request request = new Request.DeleteAccount(player);
        Server.getInstance().addRequest(request);
        player = null;
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

        public void exit(){
            Client.this.exit();
        }
    }

    public class MainMenuAction {
        public void exit(){
            Client.this.logout();
            Client.this.exit();
        }

        public void logout(){
            Client.this.logout();
            // reset panels
            now = PanelType.LOGIN_PANEL;
            updateFramePanel();
        }
        public void deleteAccount(){
            Client.this.deleteAccount();
            // reset panels
            now = PanelType.LOGIN_PANEL;
            updateFramePanel();
        }
    }



}
