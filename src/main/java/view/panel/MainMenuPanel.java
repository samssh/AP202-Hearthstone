package view.panel;

import client.Client;
import configs.Config;
import configs.ConfigFactory;
import lombok.Setter;
import server.Server;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    @Setter
    private String playerName;
    private JLabel welcome;
    private JButton play, shop, status, collection, exit, logout, deleteAccount;
    private int componentWidth, componentHeight, componentSpace;
    private int exitWidth, exitHeight, exitX, exitY, exitSpace, shiftX, shiftY;
    private final Dimension dimension;
    private final Client.MainMenuAction mainMenuAction;

    public MainMenuPanel(Client.MainMenuAction mainMenuAction) {
        setLayout(null);
        config();
        this.mainMenuAction = mainMenuAction;
        dimension = new Dimension(componentWidth, componentHeight);
        initialize();
        this.add(welcome);
        this.add(play);
        this.add(shop);
        this.add(status);
        this.add(collection);
        this.add(exit);
        this.add(logout);
        this.add(deleteAccount);
    }

    private void config() {
        Config panelConfig = ConfigFactory.getInstance("").getConfig("MAIN_MENU_CONFIG");
        componentWidth = panelConfig.getProperty(Integer.class, "componentWidth");
        componentHeight = panelConfig.getProperty(Integer.class, "componentHeight");
        componentSpace = panelConfig.getProperty(Integer.class, "componentSpace");
        setBounds(panelConfig.getProperty(Integer.class, "x"),
                panelConfig.getProperty(Integer.class, "y"),
                panelConfig.getProperty(Integer.class, "width"),
                panelConfig.getProperty(Integer.class, "height"));
        exitX = panelConfig.getProperty(Integer.class, "exitX");
        exitY = panelConfig.getProperty(Integer.class, "exitY");
        exitWidth = panelConfig.getProperty(Integer.class, "exitWidth");
        exitHeight = panelConfig.getProperty(Integer.class, "exitHeight");
        exitSpace = panelConfig.getProperty(Integer.class, "exitSpace");
        shiftX = panelConfig.getProperty(Integer.class, "shiftX");
        shiftY = panelConfig.getProperty(Integer.class, "shiftY");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        super.paintComponent(graphics2D);
        welcome.setText("welcome " + playerName);
        int sumHeight = componentHeight + componentSpace;
        int startX = (this.getWidth() - componentWidth) / 2 + shiftX;
        int startY = this.getHeight() / 2 - (4 * sumHeight + componentHeight) / 2 + shiftY;
        welcome.setLocation(startX, startY);
        play.setLocation(startX, startY + sumHeight);
        shop.setLocation(startX, startY + 2 * sumHeight);
        status.setLocation(startX, startY + 3 * sumHeight);
        collection.setLocation(startX, startY + 4 * sumHeight);
    }

    private void initialize() {
        initializeWelcome();
        initializeShop();
        initializePlay();
        initializeStatus();
        initializeCollection();
        initializeExit();
        initializeLogout();
        initializeDeleteAccount();
    }

    private void initializeWelcome() {
        welcome = new JLabel("welcome " + playerName, SwingConstants.CENTER);
        welcome.setSize(dimension);
    }

    private void initializePlay() {
        play = new JButton("play");
        play.setSize(dimension);
    }

    private void initializeShop() {
        shop = new JButton("shop");
        shop.setSize(dimension);
    }

    private void initializeStatus() {
        status = new JButton("status");
        status.setSize(dimension);
    }

    private void initializeCollection() {
        collection = new JButton("collection");
        collection.setSize(dimension);
    }

    private void initializeExit() {
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(actionEvent -> mainMenuAction.exit());
    }

    private void initializeLogout() {
        logout = new JButton("logout");
        int y = exitY - 2 * (exitHeight + exitSpace);
        logout.setBounds(exitX, y, exitWidth, exitHeight);
        logout.addActionListener(actionEvent -> mainMenuAction.logout());
    }

    private void initializeDeleteAccount() {
        deleteAccount = new JButton("delete account");
        int y = exitY - (exitHeight + exitSpace);
        deleteAccount.setBounds(exitX, y, exitWidth, exitHeight);
        deleteAccount.addActionListener(actionListener -> mainMenuAction.deleteAccount());
    }


//    private void resetComponents() {
////        welcome.setText("welcome");
////        userName.setText("Enter username");
////        password.setText("Enter password");
////        password.setEchoChar((char) 0);
////        passwordAgain.setText("Enter password");
////        passwordAgain.setEchoChar((char) 0);
//    }

    public void setMessage(String message) {
        welcome.setText(message);
    }
//
//    public void reset() {
////        mode = view.panel.LoginPanel.Mode.SIGN_IN;
////        passwordAgain.setVisible(false);
////        resetComponents();
//    }
}
