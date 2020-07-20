package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectHeroPower extends Request{
    private final int side;

    public SelectHeroPower(int side) {
        this.side = side;
    }

    @Override
    public void execute(Server server) {
        server.selectHeroPower(side);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
