package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectCardOnPassive extends Request{
    private final int index;

    public SelectCardOnPassive(int index) {
        this.index = index;
    }

    @Override
    public void execute(Server server) {
        server.selectCadOnPassive(index);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
