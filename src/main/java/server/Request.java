package server;

import controller.Executable;
import model.Card;
import model.Player;
import view.panel.LoginPanel;

public abstract class Request implements Executable {
    public abstract void execute();

    public static class LoginRequest extends Request {
        private final String userName, password;
        private final LoginPanel.Mode mode;

        public LoginRequest(String userName, String password, LoginPanel.Mode mode) {
            this.userName = userName;
            this.password = password;
            this.mode = mode;
        }

        @Override
        public void execute() {
            Server.getInstance().login(userName, password, mode);
        }
    }

    public static class LogoutRequest extends Request {
        @Override
        public void execute() {
            Server.getInstance().logout();
        }
    }

    public static class DeleteAccount extends Request {
        @Override
        public void execute() {
            Server.getInstance().deleteAccount();
        }
    }

    public static class Shop extends Request {
        @Override
        public void execute() {
            Server.getInstance().sendShop();
        }
    }

    public static class SellCard extends Request {
        private final Card card;

        public SellCard(Card card) {
            this.card = card;
        }

        @Override
        public void execute() {
            Server.getInstance().sellCard(card);
        }
    }

    public static class BuyCard extends Request {
        private final Card card;

        public BuyCard(Card card) {
            this.card = card;
        }

        @Override
        public void execute() {
            Server.getInstance().buyCard(card);
        }
    }


}
