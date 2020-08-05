package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.controller.network.SocketRequestSender;
import ir.sam.hearthstone.client.view.panel.ConnectPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ConnectAction {
    private final Client client;

    public ConnectAction(Client client) {
        this.client = client;
    }


    public void connect(ConnectPanel connectPanel) {
        int port;
        try {
            port = Integer.parseInt(connectPanel.getPort().getText());
        } catch (NumberFormatException e) {
            return;
        }
        String address = connectPanel.getAddress().getText();
        if ("Enter address".equals(address) || "".equals(address))
            return;
        // try connect
        connectPanel.getConnect().setText("connecting");
        for (ActionListener al : connectPanel.getConnect().getActionListeners()) {
            connectPanel.getConnect().removeActionListener(al);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                Socket socket = new Socket(address, port);
                client.setRequestSender(new SocketRequestSender(socket));
                client.setOnLogin();
            } catch (IOException io) {
                io.printStackTrace();
                connectPanel.getConnect().setText("connect");
                connectPanel.getConnect().addActionListener(e -> this.connect(connectPanel));
            }
        });

    }
}
