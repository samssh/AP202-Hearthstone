package ir.SAM.hearthstone.requests;

import ir.SAM.hearthstone.server.Server;

public class ShutdownRequest extends Request {
    @Override
    public void execute(Server server) {
        server.shutdown();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setShutdownRequest(this);
    }
}
