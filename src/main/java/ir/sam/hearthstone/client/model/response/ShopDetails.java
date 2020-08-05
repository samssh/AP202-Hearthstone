package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ShopDetails extends Response {
    @Getter
    @Setter
    private List<CardOverview> sell, buy;
    @Getter
    @Setter
    private int coins;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setShopDetails(sell, buy, coins);
    }
}