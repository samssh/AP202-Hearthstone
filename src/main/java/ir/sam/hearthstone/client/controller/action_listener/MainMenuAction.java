package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.model.log.ButtonLog;
import ir.sam.hearthstone.client.util.Updatable;
import ir.sam.hearthstone.client.util.hibernate.Connector;
import ir.sam.hearthstone.client.view.PanelType;
import ir.sam.hearthstone.client.view.panel.MainMenuPanel;
import ir.sam.hearthstone.client.view.panel.PlayPanel;

import static ir.sam.hearthstone.client.view.PanelType.*;

public class MainMenuAction {
    private final Connector connector;
    private final Client client;

    public MainMenuAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", MAIN_MENU.toString()));
        client.sendLogoutRequest();
        client.exit();
    }

    public void logout() {
        connector.save(new ButtonLog(client.getUsername(), "logout", MAIN_MENU.toString()));
        client.sendLogoutRequest();
    }

    public void deleteAccount() {
        connector.save(new ButtonLog(client.getUsername(), "deleteAccount", MAIN_MENU.toString()));
        client.deleteAccount();
    }

    public void shop() {
        connector.save(new ButtonLog(client.getUsername(), "shop", MAIN_MENU.toString()));
        client.sendShopRequest();
    }

    public void status() {
        connector.save(new ButtonLog(client.getUsername(), "status", MAIN_MENU.toString()));
        client.sendStatusRequest();
    }

    public void collection() {
        connector.save(new ButtonLog(client.getUsername(), "collection", MAIN_MENU.toString()));
        ((Updatable) client.getPanels().get(COLLECTION)).update();
    }

    public void play(String mode) {
        client.getPanels().put(PLAY, new PlayPanel(new PlayAction(connector, client)));
        connector.save(new ButtonLog(client.getUsername(), mode, MAIN_MENU.toString()));
        client.sendStartPlayRequest(mode);
    }

    public void update() {
        ((MainMenuPanel) client.getPanels().get(PanelType.MAIN_MENU)).setMessage("welcome " + client.getUsername());
    }
}