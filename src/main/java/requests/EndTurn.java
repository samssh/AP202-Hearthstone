package requests;

import server.Server;

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