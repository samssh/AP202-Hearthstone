package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class EndTurn extends Request {

    @Override
    public void execute(Server server) {
        server.endTurn();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setEndTurn(this);
    }
}