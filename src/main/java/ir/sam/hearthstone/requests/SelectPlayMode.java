package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectPlayMode extends Request{
    private final String modeName;

    public SelectPlayMode(String modeName) {
        this.modeName = modeName;
    }

    @Override
    public void execute(Server server) {
        server.selectPlayMode(modeName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
