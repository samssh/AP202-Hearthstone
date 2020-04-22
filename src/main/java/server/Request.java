package server;

import controller.Executable;
import model.Player;
import view.panel.LoginPanel;

public abstract class Request implements Executable {
    public abstract void execute();
    public static class LoginRequest extends Request{
        private final String userName,password;
        private final LoginPanel.Mode mode;

        public LoginRequest(String userName, String password,LoginPanel.Mode mode) {
            this.userName = userName;
            this.password = password;
            this.mode=mode;
        }

        @Override
        public void execute() {
            Server.getInstance().login(userName,password,mode);
        }
    }
    public static class LogoutRequest extends Request{
        private final Player player;

        public LogoutRequest(Player player) {
            this.player=player;
        }

        @Override
        public void execute() {
            Server.getInstance().logout(player);
        }
    }
    public static class DeleteAccount extends Request{
        private final Player player;

        public DeleteAccount(Player player) {
            this.player=player;
        }

        @Override
        public void execute() {
            Server.getInstance().deleteAccount(player);
        }
    }
}
