package requests;

import server.Server;

public class DeleteAccount extends Request {
    @Override
    public void execute() {
        Server.getInstance().deleteAccount();
    }
}