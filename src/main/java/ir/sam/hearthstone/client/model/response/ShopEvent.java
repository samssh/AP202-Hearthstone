package ir.sam.hearthstone.client.model.response;

public class ShopEvent extends Response {
    private final String cardName;
    private final String type;
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
