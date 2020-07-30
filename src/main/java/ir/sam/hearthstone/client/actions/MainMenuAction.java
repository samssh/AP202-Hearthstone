package ir.sam.hearthstone.client.actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.ButtonLog;
import ir.sam.hearthstone.util.Updatable;
import ir.sam.hearthstone.view.PanelType;
import ir.sam.hearthstone.view.panel.CollectionPanel;
import ir.sam.hearthstone.view.panel.MainMenuPanel;
import ir.sam.hearthstone.view.panel.PlayPanel;

import static ir.sam.hearthstone.view.PanelType.*;

public class MainMenuAction {
    private final Connector connector;
    private final Client client;

    public MainMenuAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", MAIN_MENU.toString()));
        client.logout();
        client.exit();
    }

    public void logout() {
        connector.save(new ButtonLog(client.getUsername(), "logout", MAIN_MENU.toString()));
        client.logout();
        ((CollectionPanel) client.getPanels().get(COLLECTION)).reset();
        client.setNow(PanelType.LOGIN);
        client.updateFrame();
        client.getHistory().clear();
    }

    public void deleteAccount() {
        connector.save(new ButtonLog(client.getUsername(), "deleteAccount", MAIN_MENU.toString()));
        client.deleteAccount();
        ((CollectionPanel) client.getPanels().get(COLLECTION)).reset();
        client.setNow(PanelType.LOGIN);
        client.updateFrame();
        client.getHistory().clear();
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

    public void play() {
        client.getPanels().put(PLAY,new PlayPanel(new PlayAction(connector,client)));
        connector.save(new ButtonLog(client.getUsername(), "play", MAIN_MENU.toString()));
        client.sendStartPlayRequest();
    }

    public void update() {
        ((MainMenuPanel) client.getPanels().get(PanelType.MAIN_MENU)).setMessage("welcome " + client.getUsername());
    }
}