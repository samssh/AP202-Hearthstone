package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.model.log.ButtonLog;
import ir.sam.hearthstone.client.model.requests.BuyCard;
import ir.sam.hearthstone.client.model.requests.SellCard;
import ir.sam.hearthstone.client.util.hibernate.Connector;

import static ir.sam.hearthstone.client.view.PanelType.SHOP;

public class ShopAction {
    private final Connector connector;
    private final Client client;

    public ShopAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void sell(String cardName) {
        client.addRequest(new SellCard(cardName));
        connector.save(new ButtonLog(client.getUsername(), "sell:" + cardName, SHOP.toString()));
    }

    public void buy(String cardName) {
        client.addRequest(new BuyCard(cardName));
        connector.save(new ButtonLog(client.getUsername(), "buy:" + cardName, SHOP.toString()));
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", SHOP.toString()));
        client.sendLogoutRequest();
        client.exit();
    }

    public void back() {
        connector.save(new ButtonLog(client.getUsername(), "back", SHOP.toString()));
        client.back();
    }

    public void backMainMenu() {
        connector.save(new ButtonLog(client.getUsername(), "backMainMenu", SHOP.toString()));
        client.backMainMenu();
    }

    public void update() {
        client.sendShopRequest();
    }
}
