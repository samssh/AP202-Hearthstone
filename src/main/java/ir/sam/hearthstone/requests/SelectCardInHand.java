package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import lombok.Getter;

public class SelectCardInHand extends Request {
    @Getter
    private final int side,index;

    public SelectCardInHand(int side, int index) {
        this.side = side;
        this.index = index;
    }

    @Override
    public void execute(Server server) {

    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setPlayCard(this);
    }
}