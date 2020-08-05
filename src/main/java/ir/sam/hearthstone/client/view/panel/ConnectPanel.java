package ir.sam.hearthstone.client.view.panel;

import ir.sam.hearthstone.client.controller.action_listener.ConnectAction;
import ir.sam.hearthstone.client.view.util.Constant;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ConnectPanel extends JPanel {
    @Getter
    private JTextField port, address;
    @Getter
    private JButton connect;
    private final Dimension dimension;
    private final ConnectAction connectAction;

    public ConnectPanel(ConnectAction connectAction) {
        dimension = new Dimension(120, 60);
        this.connectAction = connectAction;
        init();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(grid);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(address, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(port, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(connect, gbc);
    }

    private void init() {
        initPort();
        initAddress();
        initConnect();
    }

    private void initPort() {
        port = new JTextField("Enter port");
        port.setPreferredSize(dimension);
        port.setSize(dimension);
        port.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Enter port".equals(port.getText())) {
                    port.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(port.getText())) {
                    port.setText("Enter port");
                }
            }
        });
        Constant.makeWhite(port);
    }

    private void initAddress() {
        address = new JTextField("Enter address");
        address.setPreferredSize(dimension);
        address.setSize(dimension);
        address.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Enter address".equals(address.getText())) {
                    address.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(address.getText())) {
                    address.setText("Enter address");
                }
            }
        });
        Constant.makeWhite(address);
    }

    private void initConnect() {
        connect = new JButton("connect");
        connect.setPreferredSize(dimension);
        connect.setSize(dimension);
        Constant.makeTransparent(connect);
        connect.addActionListener(e -> connectAction.connect(this));
    }


}
