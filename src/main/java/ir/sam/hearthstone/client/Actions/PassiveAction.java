package ir.sam.hearthstone.client.Actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.ButtonLog;
import ir.sam.hearthstone.model.log.RequestLog;
import ir.sam.hearthstone.requests.*;

import static ir.sam.hearthstone.view.PanelType.PASSIVE;

public class PassiveAction {
    private final Connector connector;
    private final Client client;

    public PassiveAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", PASSIVE.toString()));
        client.logout();
        client.exit();
    }

    public void back() {
        connector.save(new ButtonLog(client.getUsername(), "back", PASSIVE.toString()));
        client.back();
    }

    public void backMainMenu() {
        connector.save(new ButtonLog(client.getUsername(), "backMainMenu", PASSIVE.toString()));
        client.backMainMenu();
    }

    public void selectPassive(String passiveName) {
        Request request = new SelectPassive(passiveName);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername(), "passive:" + passiveName, PASSIVE.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }
}