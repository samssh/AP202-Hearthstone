package requests;

import server.Server;

public class EndTurn extends Request {

    @Override
    public void execute() {
        Server.getInstance().endTurn();
    }
}