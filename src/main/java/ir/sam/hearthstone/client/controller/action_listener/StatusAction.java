package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.model.log.ButtonLog;
import ir.sam.hearthstone.client.util.hibernate.Connector;

import static ir.sam.hearthstone.client.view.PanelType.STATUS;

public class StatusAction {
    private final Connector connector;
    private final Client client;

    public StatusAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", STATUS.toString()));
        client.sendLogoutRequest();
        client.exit();
    }

    public void back() {
        connector.save(new ButtonLog(client.getUsername(), "back", STATUS.toString()));
        client.back();
    }

    public void backMainMenu() {
        connector.save(new ButtonLog(client.getUsername(), "backMainMenu", STATUS.toString()));
        client.backMainMenu();
    }

    public void update() {
        client.sendStatusRequest();
    }
}