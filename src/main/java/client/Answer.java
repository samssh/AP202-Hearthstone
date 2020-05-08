package client;

import lombok.Getter;
import view.model.*;

import java.util.List;

public abstract class Answer {
    abstract void execute();

    public static class LoginAnswer extends Answer {
        @Getter
        private final boolean success;
        @Getter
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
        void execute() {
            Client.getInstance().setShopDetails(sell, buy, coins);
        }
    }

    public static class StatusDetails extends Answer {
        @Getter
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
        @Getter
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
        @Getter
        private final List<CardOverview> cards;
        @Getter
        private final List<SmallDeckOverview> decks;
        @Getter
        private final List<CardOverview> deckCards;
        @Getter
        private final boolean canAddDeck, canChangeHero;
        @Getter
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
        @Getter
        private final String message;

        public showMessage(String message) {
            this.message = message;
        }

        @Override
        void execute() {
            Client.getInstance().showMessage(message);
        }
    }

    public static class GoTo extends Answer {
        @Getter
        private final String panel, message;

        public GoTo(String panel, String message) {
            this.panel = panel;
            this.message = message;
        }


        @Override
        void execute() {
            Client.getInstance().goTo(panel, message);
        }
    }

    public static class PassiveDetails extends Answer {
        @Getter
        private final List<PassiveOverview> passives;

        public PassiveDetails(List<PassiveOverview> passives) {
            this.passives = passives;
        }

        @Override
        void execute() {
            Client.getInstance().setPassives(passives);
        }
    }

    public static class PlayDetails extends Answer {
        @Getter
        private final List<CardOverview> hand, ground;
        @Getter
        private final CardOverview weapon;
        @Getter
        private final HeroOverview hero;
        @Getter
        private final HeroPowerOverview heroPower;
        @Getter
        private final String eventLog;
        @Getter
        private final int mana, deckCards;

        public PlayDetails(List<CardOverview> hand, List<CardOverview> ground, CardOverview weapon,
                           HeroOverview hero, HeroPowerOverview heroPower, String eventLog, int mana, int deckCards) {
            this.hand = hand;
            this.ground = ground;
            this.weapon = weapon;
            this.hero = hero;
            this.heroPower = heroPower;
            this.eventLog = eventLog;
            this.mana = mana;
            this.deckCards = deckCards;
        }

        @Override
        void execute() {
            Client.getInstance().setPlayDetail(hand,ground,weapon,hero,heroPower,eventLog,mana,deckCards);
        }
    }
}

