package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

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