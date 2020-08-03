package ir.sam.hearthstone.client.model.requests;

import lombok.Getter;

public class SelectCardInHand extends Request {
    @Getter
    private final int side, index;

    public SelectCardInHand(int side, int index) {
        this.side = side;
        this.index = index;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectCardInHand(side, index);
    }
}