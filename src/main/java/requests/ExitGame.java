package requests;

import server.Server;

public class ExitGame extends Request {

    @Override
    public void execute(Server server) {
        server.exitGame();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setExitGame(this);
    }
}