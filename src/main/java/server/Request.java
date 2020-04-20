package server;

import controller.Executable;
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
}
