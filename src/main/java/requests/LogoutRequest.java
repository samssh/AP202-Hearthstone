package requests;

import server.Server;

public class LogoutRequest extends Request {
    @Override
    public void execute() {
        Server.getInstance().logout();
    }
}