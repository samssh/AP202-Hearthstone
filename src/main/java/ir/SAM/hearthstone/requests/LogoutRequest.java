package ir.SAM.hearthstone.requests;

import ir.SAM.hearthstone.server.Server;

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