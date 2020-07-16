package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectHero extends Request{
    private final int side;

    public SelectHero(int side) {
        this.side = side;
    }

    @Override
    public void execute(Server server) {

    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
