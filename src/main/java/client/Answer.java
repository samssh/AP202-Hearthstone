package client;

import util.Executable;
import view.model.CardOverview;
import view.model.DeckOverview;

import java.util.List;

public abstract class Answer implements Executable {
    public static class LoginAnswer extends Answer {
        private final boolean success;
        private final String message;

        public LoginAnswer(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        public void execute() {
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
        public void execute() {
            Client.getInstance().setShopDetails(sell, buy, coins);
        }
    }

    public static class StatusDetails extends Answer {
        private final List<DeckOverview> deckOverviews;

        public StatusDetails(List<DeckOverview> deckOverviews) {
            this.deckOverviews = deckOverviews;
        }

        @Override
        public void execute() {
            Client.getInstance().setStatusDetails(deckOverviews);
        }
    }

    public static class FirstCollectionDetails extends Answer {
        private final List<String> heroNames;

        public FirstCollectionDetails(List<String> heroNames) {
            this.heroNames = heroNames;
        }

        @Override
        public void execute() {
            Client.getInstance().setFirstCollectionDetail(heroNames);
        }
    }

    public static class CollectionDetails extends Answer {
        private final List<CardOverview> cards;
        private final List<DeckOverview> decks;
        private final List<CardOverview> deckCards;
        private final boolean canAddDeck ,canChangeHero;

        public CollectionDetails(List<CardOverview> cards, List<DeckOverview> decks,
                                 List<CardOverview> deckCards, boolean canAddDeck, boolean canChangeHero) {
            this.cards = cards;
            this.decks = decks;
            this.deckCards = deckCards;
            this.canAddDeck = canAddDeck;
            this.canChangeHero = canChangeHero;
        }

        @Override
        public void execute() {
            Client.getInstance().setCollectionDetail(cards, decks,deckCards,canAddDeck,canChangeHero);
        }
    }
}
