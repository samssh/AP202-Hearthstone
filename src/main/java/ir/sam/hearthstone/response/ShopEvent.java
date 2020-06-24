package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;

public class ShopEvent extends Response{
    private final String cardName;
    private final String type;

    public ShopEvent(String cardName, String type) {
        this.cardName = cardName;
        this.type = type;
    }

    @Override
    public void execute(Client client) {
        client.putShopEvent(cardName,type);
    }

    @Override
    public void accept(ResponseLogInfoVisitor responseLogInfoVisitor) {

    }
}
