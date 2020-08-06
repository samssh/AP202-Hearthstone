package ir.sam.hearthstone.server.model.response;

import lombok.Getter;

public class ShopEvent extends Response {
    @Getter
    private final String cardName;
    @Getter
    private final String type;
    @Getter
    private final int coins;

    public ShopEvent(String cardName, String type, int coins) {
        this.cardName = cardName;
        this.type = type;
        this.coins = coins;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.putShopEvent(cardName, type, coins);
    }
}
