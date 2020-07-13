package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class ConfirmOnPassive extends Request{
    @Override
    public void execute(Server server) {
        server.confirm();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
