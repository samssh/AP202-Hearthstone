package client;

import util.Executable;
import view.model.CardOverview;
import view.model.DeckOverview;

import java.util.List;

public abstract class Answer implements Executable {
    public abstract void execute();
    public static class LoginAnswer extends Answer {
        private final boolean success;
        private final String message;

        public LoginAnswer(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        public void execute() {
            Client.getInstance().login(success,message);
        }
    }

    public static class ShopDetails extends Answer {
        private final List<CardOverview> sell,buy;
        private final int coins;

        public ShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coins) {
            this.sell = sell;
            this.buy = buy;
            this.coins = coins;
        }

        @Override
        public void execute() {
            Client.getInstance().setShopDetails(sell,buy,coins);
        }
    }

    public static class StatusDetail extends Answer{
        private final List<DeckOverview> deckOverviews;

        public StatusDetail(List<DeckOverview> deckOverviews) {
            this.deckOverviews = deckOverviews;
        }

        @Override
        public void execute() {
            Client.getInstance().setStatusDetails(deckOverviews);
        }
    }
}
