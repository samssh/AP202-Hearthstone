package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

public class ShopEvent extends Response {
    @Getter
    @Setter
    private String cardName;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private int coins;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.putShopEvent(cardName, type, coins);
    }
}
