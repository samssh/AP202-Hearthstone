package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class StatusRequest extends Request {

    @Override
    public void execute(Server server) {
        server.sendStatus();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setStatus(this);
    }
}