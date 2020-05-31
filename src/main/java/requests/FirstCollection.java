package requests;

import server.Server;

public class FirstCollection extends Request {

    @Override
    public void execute() {
        Server.getInstance().sendFirstCollection();
    }
}