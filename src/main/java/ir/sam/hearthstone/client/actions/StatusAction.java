package ir.sam.hearthstone.client.actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.ButtonLog;

import static ir.sam.hearthstone.view.PanelType.STATUS;

public class StatusAction {
    private final Connector connector;
    private final Client client;

    public StatusAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", STATUS.toString()));
        client.logout();
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