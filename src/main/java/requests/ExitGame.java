package requests;

import server.Server;

public class ExitGame extends Request {

    @Override
    public void execute() {
        Server.getInstance().exitGame();
    }
}