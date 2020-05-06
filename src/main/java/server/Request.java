package server;

import view.panel.LoginPanel;

public abstract class Request {
    abstract void execute();

    public static class LoginRequest extends Request {
        private final String userName, password;
        private final int mode;

        public LoginRequest(String userName, String password, int mode) {
            this.userName = userName;
            this.password = password;
            this.mode = mode;
        }

        @Override
        void execute() {
            Server.getInstance().login(userName, password, mode);
        }
    }

    public static class LogoutRequest extends Request {
        @Override
        void execute() {
            Server.getInstance().logout();
        }
    }

    public static class DeleteAccount extends Request {
        @Override
        void execute() {
            Server.getInstance().deleteAccount();
        }
    }

    public static class Shop extends Request {
        @Override
        void execute() {
            Server.getInstance().sendShop();
        }
    }

    public static class SellCard extends Request {
        private final String cardName;

        public SellCard(String cardName) {
            this.cardName = cardName;
        }

        @Override
        void execute() {
            Server.getInstance().sellCard(cardName);
        }
    }

    public static class BuyCard extends Request {
        private final String cardName;

        public BuyCard(String cardName) {
            this.cardName = cardName;
        }

        @Override
        void execute() {
            Server.getInstance().buyCard(cardName);
        }
    }

    public static class Status extends Request {

        @Override
        void execute() {
            Server.getInstance().sendStatus();
        }
    }

    public static class FirstCollection extends Request {

        @Override
        void execute() {
            Server.getInstance().sendFirstCollection();
        }
    }

    public static class CollectionDetails extends Request {
        private final String name, classOfCard, deckName;
        private final int mana, lockMode;

        public CollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
            this.name = name;
            this.classOfCard = classOfCard;
            this.deckName = deckName;
            this.mana = mana;
            this.lockMode = lockMode;
        }

        @Override
        void execute() {
            Server.getInstance().sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);
        }
    }

    public static class NewDeck extends Request {
        private final String deckName, heroName;

        public NewDeck(String deckName, String heroName) {
            this.deckName = deckName;
            this.heroName = heroName;
        }

        @Override
        void execute() {
            Server.getInstance().newDeck(deckName, heroName);
        }
    }

    public static class DeleteDeck extends Request {
        private final String deckName;

        public DeleteDeck(String deckName) {
            this.deckName = deckName;
        }

        @Override
        void execute() {
            Server.getInstance().deleteDeck(deckName);
        }
    }

    public static class ChangeDeckName extends Request {
        private final String oldDeckName, newDeckName;

        public ChangeDeckName(String oldDeckName, String newDeckName) {
            this.oldDeckName = oldDeckName;
            this.newDeckName = newDeckName;
        }

        @Override
        void execute() {
            Server.getInstance().changeDeckName(oldDeckName, newDeckName);
        }
    }

    public static class ChangeHeroDeck extends Request {
        private final String deckName, heroName;

        public ChangeHeroDeck(String deckName, String heroName) {
            this.deckName = deckName;
            this.heroName = heroName;
        }

        @Override
        void execute() {
            Server.getInstance().changeHeroDeck(deckName, heroName);
        }
    }

    public static class AddCardToDeck extends Request {
        private final String cardName, deckName;

        public AddCardToDeck(String cardName, String deckName) {
            this.cardName = cardName;
            this.deckName = deckName;
        }

        @Override
        void execute() {
            Server.getInstance().addCardToDeck(cardName, deckName);
        }
    }

    public static class RemoveCardFromDeck extends Request {
        private final String cardName, deckName;

        public RemoveCardFromDeck(String cardName, String deckName) {
            this.cardName = cardName;
            this.deckName = deckName;
        }

        @Override
        void execute() {
            Server.getInstance().removeCardFromDeck(cardName, deckName);
        }
    }

    public static class StartPlaying extends Request {
        @Override
        void execute() {
            Server.getInstance().startPlay();
        }
    }
}
