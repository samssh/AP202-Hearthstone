package view.panel;

import client.Client;
import configs.Config;
import configs.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginPanel extends JPanel {
    @Setter
    @Getter
    private Mode mode;
    private JLabel welcome;
    private JTextField userName;
    private JPasswordField password, passwordAgain;
    private JButton login, changeMode, exit;
    private int componentWidth, componentHeight, componentSpace;
    private int exitWidth, exitHeight, exitX, exitY, shiftX, shiftY;
    private final Dimension dimension;
    private final Client.LoginPanelAction loginPanelAction;
    private Image bg;

    public LoginPanel(Client.LoginPanelAction loginPanelAction) {
        setLayout(null);
        config();
        dimension = new Dimension(componentWidth, componentHeight);
        initialize();
        this.loginPanelAction = loginPanelAction;
        mode = Mode.SIGN_IN;
        this.add(welcome);
        this.add(userName);
        this.add(password);
        this.add(passwordAgain);
        this.add(login);
        this.add(changeMode);
        this.add(exit);
    }

    private void initialize() {
        initializeChangeMode();
        initializeWelcome();
        initializeLoginB();
        initializeUsername();
        initializePasswords();
        initializeExit();
    }

    private void initializeExit() {
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(actionListener -> loginPanelAction.exit());
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        exit.setFocusable(false);
        exit.setForeground(Color.RED);
    }

    private void initializeWelcome() {
        welcome = new JLabel("welcome", SwingConstants.CENTER);
        welcome.setSize(dimension);
        welcome.setForeground(Color.RED);
//        welcome.setFocusable(false);
    }

    private void initializeLoginB() {
        login = new JButton();
        login.setSize(dimension);
        login.addActionListener(actionListener -> loginPanelAction.login(
                this,userName.getText(),String.valueOf(password.getPassword()),String.valueOf(passwordAgain.getPassword())));
        login.setOpaque(false);
        login.setContentAreaFilled(false);
        login.setBorderPainted(false);
        login.setFocusable(false);
        login.setForeground(Color.RED);
    }



    private void initializeChangeMode() {
        changeMode = new JButton();
        changeMode.setSize(dimension);
        changeMode.addActionListener(actionEvent -> loginPanelAction.changeMode(this));
        changeMode.setOpaque(false);
        changeMode.setContentAreaFilled(false);
        changeMode.setBorderPainted(false);
        changeMode.setFocusable(false);
        changeMode.setForeground(Color.RED);
    }

    private void initializeUsername() {
        userName = new JTextField("Enter username");
        userName.setSize(dimension);
        userName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Enter username".equals(userName.getText())) {
                    userName.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(userName.getText())) {
                    userName.setText("Enter username");
                }
            }
        });
        userName.setOpaque(false);
        userName.setForeground(Color.red);
        userName.setBorder(null);
        userName.setFont(userName.getFont().deriveFont(Font.BOLD));
    }

    private void initializePasswords() {
        password = new JPasswordField();
        password.setSize(dimension);
        initializePass(password);
        passwordAgain = new JPasswordField();
        passwordAgain.setVisible(false);
        passwordAgain.setSize(dimension);
        initializePass(passwordAgain);
    }

    private void initializePass(JPasswordField password1) {
        char passwordChar = password1.getEchoChar();
        password1.setEchoChar((char) 0);
        password1.setText("Enter password");
        password1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Enter password".equals(String.valueOf(password1.getPassword()))) {
                    password1.setText("");
                    password1.setEchoChar(passwordChar);

                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(String.valueOf(password1.getPassword()))) {
                    password1.setText("Enter password");
                    password1.setEchoChar((char) 0);
                }
            }
        });
        password1.setOpaque(false);
        password1.setForeground(Color.RED);
        password1.setBorder(null);
        password1.setFont(password1.getFont().deriveFont(Font.BOLD));
    }

    private void resetComponents() {
        welcome.setText("welcome");
        userName.setText("Enter username");
        welcome.requestFocus();
        password.setText("Enter password");
        password.setEchoChar((char) 0);
        passwordAgain.setText("Enter password");
        passwordAgain.setEchoChar((char) 0);
    }

    public void setMessage(String message) {
        welcome.setText(message);
    }

    public void reset() {
        if (mode == Mode.SIGN_IN)
            passwordAgain.setVisible(false);
        if (mode == Mode.SIGN_UP)
            passwordAgain.setVisible(true);
        resetComponents();
    }

    public enum Mode {
        SIGN_IN(1), SIGN_UP(2);
        @Getter
        int value;
        Mode(int value){
            this.value = value;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        super.paintComponent(graphics2D);
        graphics2D.drawImage(bg,0,0,getWidth(),getHeight(),this);
        int startWidth = (this.getWidth() - componentWidth) / 2 + shiftX;
        int startHeight = this.getHeight() / 2 + shiftY;
        int sumHeight = componentHeight + componentSpace;
        if (mode == Mode.SIGN_IN) {
            startHeight = startHeight - (4 * sumHeight + componentHeight) / 2;
            login.setLocation(startWidth, startHeight + 3 * sumHeight);
            changeMode.setLocation(startWidth, startHeight + 4 * sumHeight);
            login.setText("sign in");
            changeMode.setText("sign up");
        }
        if (mode == Mode.SIGN_UP) {
            startHeight = startHeight - (5 * sumHeight + componentHeight) / 2;
            passwordAgain.setLocation(startWidth, startHeight + 3 * sumHeight);
            login.setLocation(startWidth, startHeight + 4 * sumHeight);
            changeMode.setLocation(startWidth, startHeight + 5 * sumHeight);
            login.setText("sign up");
            changeMode.setText("sign in");
        }
        welcome.setLocation(startWidth, startHeight);
        userName.setLocation(startWidth, startHeight + sumHeight);
        password.setLocation(startWidth, startHeight + 2 * sumHeight);
    }

    private void config() {
        Config panelConfig = ConfigFactory.getInstance().getConfig("LOGIN_PANEL_CONFIG");
        setBounds(panelConfig.getProperty(Integer.class, "x"),
                panelConfig.getProperty(Integer.class, "y"),
                panelConfig.getProperty(Integer.class, "width"),
                panelConfig.getProperty(Integer.class, "height"));
        componentWidth = panelConfig.getProperty(Integer.class, "componentWidth");
        componentHeight = panelConfig.getProperty(Integer.class, "componentHeight");
        componentSpace = panelConfig.getProperty(Integer.class, "componentSpace");
        exitX = panelConfig.getProperty(Integer.class, "exitX");
        exitY = panelConfig.getProperty(Integer.class, "exitY");
        exitWidth = panelConfig.getProperty(Integer.class, "exitWidth");
        exitHeight = panelConfig.getProperty(Integer.class, "exitHeight");
        shiftX = panelConfig.getProperty(Integer.class, "shiftX");
        shiftY = panelConfig.getProperty(Integer.class, "shiftY");
        bg = ImageLoader.getInstance().getBackground("login");
    }
}