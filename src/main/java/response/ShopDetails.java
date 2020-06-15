package response;

import client.Client;
import lombok.Getter;
import view.model.CardOverview;

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
    public void execute(Client client) {
        client.setShopDetails(sell, buy, coins);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setShopDetailsInfo(this);
    }
}