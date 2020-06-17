package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class LogoutRequest extends Request {
    @Override
    public void execute(Server server) {
        server.logout();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setLogoutRequest(this);
    }
}