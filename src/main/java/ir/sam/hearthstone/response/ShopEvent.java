package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;

public class ShopEvent extends Response{
    private final String cardName;
    private final String type;
    private final int coins;

//    public ShopEvent(String cardName, String type) {
//        this(cardName, type, coins);
//    }

    public ShopEvent(String cardName, String type, int coins) {
        this.cardName = cardName;
        this.type = type;
        this.coins = coins;
    }

    @Override
    public void execute(Client client) {
        client.putShopEvent(cardName,type,coins);
    }

    @Override
    public void accept(ResponseLogInfoVisitor responseLogInfoVisitor) {

    }
}
