package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectMinion extends Request {
    private final int side,index, emptyIndex;

    public SelectMinion(int side, int index, int emptyIndex) {
        this.side = side;
        this.index = index;
        this.emptyIndex = emptyIndex;
    }

    @Override
    public void execute(Server server) {

    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
