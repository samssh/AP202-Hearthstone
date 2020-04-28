package server;

import util.Executable;
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
        private final String cardName;

        public SellCard(String cardName) {
            this.cardName = cardName;
        }

        @Override
        public void execute() {
            Server.getInstance().sellCard(cardName);
        }
    }

    public static class BuyCard extends Request {
        private final String cardName;

        public BuyCard(String cardName) {
            this.cardName = cardName;
        }

        @Override
        public void execute() {
            Server.getInstance().buyCard(cardName);
        }
    }

    public static class Status extends Request{

        @Override
        public void execute() {
            Server.getInstance().sendStatus();
        }
    }


}
