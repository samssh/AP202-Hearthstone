package client;

import view.model.CardOverview;
import view.model.BigDeckOverview;
import view.model.SmallDeckOverview;

import java.util.List;

public abstract class Answer {
    abstract void execute();

    public static class LoginAnswer extends Answer {
        private final boolean success;
        private final String message;

        public LoginAnswer(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        void execute() {
            Client.getInstance().login(success, message);
        }
    }

    public static class ShopDetails extends Answer {
        private final List<CardOverview> sell, buy;
        private final int coins;

        public ShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coins) {
            this.sell = sell;
            this.buy = buy;
            this.coins = coins;
        }

        @Override
        void execute() {
            Client.getInstance().setShopDetails(sell, buy, coins);
        }
    }

    public static class StatusDetails extends Answer {
        private final List<BigDeckOverview> bigDeckOverviews;

        public StatusDetails(List<BigDeckOverview> bigDeckOverviews) {
            this.bigDeckOverviews = bigDeckOverviews;
        }

        @Override
        void execute() {
            Client.getInstance().setStatusDetails(bigDeckOverviews);
        }
    }

    public static class FirstCollectionDetails extends Answer {
        private final List<String> heroNames, classOfCardNames;

        public FirstCollectionDetails(List<String> heroNames, List<String> classOfCardNames) {
            this.classOfCardNames = classOfCardNames;
            this.heroNames = heroNames;
        }

        @Override
        void execute() {
            Client.getInstance().setFirstCollectionDetail(heroNames, classOfCardNames);
        }
    }

    public static class CollectionDetails extends Answer {
        private final List<CardOverview> cards;
        private final List<SmallDeckOverview> decks;
        private final List<CardOverview> deckCards;
        private final boolean canAddDeck, canChangeHero;
        private final String deckName;

        public CollectionDetails(List<CardOverview> cards, List<SmallDeckOverview> decks,
                                 List<CardOverview> deckCards, boolean canAddDeck,
                                 boolean canChangeHero, String deckName) {
            this.cards = cards;
            this.decks = decks;
            this.deckCards = deckCards;
            this.canAddDeck = canAddDeck;
            this.canChangeHero = canChangeHero;
            this.deckName = deckName;
        }

        @Override
        void execute() {
            Client.getInstance().setCollectionDetail(cards, decks, deckCards, canAddDeck, canChangeHero, deckName);
        }
    }

    public static class showMessage extends Answer {
        private final String message;

        public showMessage(String message) {
            this.message = message;
        }

        @Override
        void execute() {
            Client.getInstance().showMessage(message);
        }
    }

    public static class GotoShop extends Answer {

        @Override
        void execute() {
            Client.getInstance().gotoShop();
        }
    }
}

