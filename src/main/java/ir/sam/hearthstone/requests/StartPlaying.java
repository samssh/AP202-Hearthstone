package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class StartPlaying extends Request {
    @Override
    public void execute(Server server) {
        server.startPlay();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setStartPlaying(this);
    }
}