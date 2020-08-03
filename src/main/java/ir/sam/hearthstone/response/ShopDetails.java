package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.view.model.CardOverview;
import lombok.Getter;

import java.util.List;

public class ShopDetails extends Response {
    @Getter
    private final List<CardOverview> sell, buy;
    @Getter
    private final int coins;

    public ShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coins) {
        this.sell = sell;
        this.buy = buy;
        this.coins = coins;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setShopDetails(sell, buy, coins);
    }
}