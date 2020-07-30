package ir.sam.hearthstone.client.actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.*;
import ir.sam.hearthstone.requests.*;

import static ir.sam.hearthstone.view.PanelType.SHOP;

public class ShopAction {
    private final Connector connector;
    private final Client client;

    public ShopAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void sell(String cardName) {
        connector.save(new ButtonLog(client.getUsername(), "sell:" + cardName, SHOP.toString()));
        Request request = new SellCard(cardName);
        client.getRequestSender().sendRequest(request);
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void buy(String cardName) {
        connector.save(new ButtonLog(client.getUsername(), "buy:" + cardName, SHOP.toString()));
        Request request = new BuyCard(cardName);
        client.getRequestSender().sendRequest(request);
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", SHOP.toString()));
        client.logout();
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
