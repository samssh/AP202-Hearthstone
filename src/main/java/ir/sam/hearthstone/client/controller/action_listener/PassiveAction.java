package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.model.log.ButtonLog;
import ir.sam.hearthstone.client.model.requests.*;
import ir.sam.hearthstone.client.util.hibernate.Connector;

import static ir.sam.hearthstone.client.view.PanelType.PASSIVE;

public class PassiveAction {
    private final Connector connector;
    private final Client client;

    public PassiveAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", PASSIVE.toString()));
        client.addRequest(new CancelGame());
        client.sendLogoutRequest();
        client.exit();
    }

    public void back() {
        connector.save(new ButtonLog(client.getUsername(), "back", PASSIVE.toString()));
        client.addRequest(new CancelGame());
        client.back();
    }

    public void backMainMenu() {
        connector.save(new ButtonLog(client.getUsername(), "backMainMenu", PASSIVE.toString()));
        client.addRequest(new CancelGame());
        client.backMainMenu();
    }

    public void selectPassive(String passiveName) {
        client.addRequest(new SelectPassive(passiveName));
        connector.save(new ButtonLog(client.getUsername(), "passive:" + passiveName, PASSIVE.toString()));
    }

    public void selectDeck(String deckName) {
        client.addRequest(new SelectOpponentDeck(deckName));
        connector.save(new ButtonLog(client.getUsername(), "deck:" + deckName, PASSIVE.toString()));
    }

    public void selectCard(String cardNumber) {
        int index = Integer.parseInt(cardNumber);
        client.addRequest(new SelectCardOnPassive(index));
        connector.save(new ButtonLog(client.getUsername(), "Card number:" + cardNumber, PASSIVE.toString()));
    }

    public void confirm() {
        client.addRequest(new ConfirmOnPassive());
        connector.save(new ButtonLog(client.getUsername(), "Confirm", PASSIVE.toString()));
    }
}