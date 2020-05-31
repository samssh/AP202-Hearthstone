package requests;

import server.Server;

public class Status extends Request {

    @Override
    public void execute() {
        Server.getInstance().sendStatus();
    }
}