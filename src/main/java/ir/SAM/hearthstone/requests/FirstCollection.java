package ir.SAM.hearthstone.requests;

import ir.SAM.hearthstone.server.Server;

public class FirstCollection extends Request {

    @Override
    public void execute(Server server) {
        server.sendFirstCollection();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setFirstCollection(this);
    }
}