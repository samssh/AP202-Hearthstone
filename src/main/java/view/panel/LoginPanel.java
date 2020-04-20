package view.panel;

import server.Request;
import server.Server;
import configs.Config;
import configs.ConfigFactory;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

public class LoginPanel extends JPanel {
    @Setter
    private Mode mode;
    private JLabel welcome;
    private JTextField userName;
    private JPasswordField password, passwordAgain;
    private JButton login, changeMode;
    private int componentWidth, componentHeight, componentSpace;
    private final Dimension dimension;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        super.paintComponent(graphics2D);
        int startWidth = (this.getWidth() - componentWidth) / 2;
        int startHeight = this.getHeight() / 2;
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


    public LoginPanel() {
        setLayout(null);
        config();
        dimension = new Dimension(componentWidth, componentHeight);
        initialize();
        mode = Mode.SIGN_IN;
        this.add(welcome);
        this.add(userName);
        this.add(password);
        this.add(passwordAgain);
        this.add(login);
        this.add(changeMode);
    }

    private void config() {
        Config panelConfig = ConfigFactory.getInstance("").getConfig("LOGIN_PANEL_CONFIG");
        setBounds(panelConfig.getProperty(Integer.class, "x"),
                panelConfig.getProperty(Integer.class, "y"),
                panelConfig.getProperty(Integer.class, "width"),
                panelConfig.getProperty(Integer.class, "height"));
        componentWidth = panelConfig.getProperty(Integer.class, "componentWidth");
        componentHeight = panelConfig.getProperty(Integer.class, "componentHeight");
        componentSpace = panelConfig.getProperty(Integer.class, "componentSpace");
    }

    private void initialize() {
        initializeChangeMode();
        initializeWelcome();
        initializeLoginB();
        initializeUsername();
        initializePasswords();
    }

    private void initializeWelcome() {
        welcome = new JLabel("welcome", SwingConstants.CENTER);
        welcome.setSize(dimension);
    }

    private void initializeLoginB() {
        login = new JButton();
        login.setSize(dimension);
        login.addActionListener(actionListener -> sendLoginRequest());
    }

    private void sendLoginRequest() {
        if (!Arrays.equals(password.getPassword(), passwordAgain.getPassword())) {
            welcome.setText("password not same");
            return;
        }
        Request request = new Request.LoginRequest(userName.getText(), String.copyValueOf(password.getPassword()), mode);
        Server.getInstance().addRequest(request);
    }

    private void initializeChangeMode() {
        changeMode = new JButton();
        changeMode.setSize(dimension);
        changeMode.addActionListener(actionEvent -> changeMode());
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
    }


    private void changeMode() {
        if (mode == Mode.SIGN_IN) {
            mode = Mode.SIGN_UP;
            passwordAgain.setVisible(true);
        } else {
            passwordAgain.setVisible(false);
            mode = Mode.SIGN_IN;
        }
        welcome.setText("welcome");
        userName.setText("Enter username");
        password.setText("Enter password");
        password.setEchoChar((char) 0);
        passwordAgain.setText("Enter password");
        passwordAgain.setEchoChar((char) 0);
    }

    public static enum Mode {
        SIGN_IN, SIGN_UP
    }
}