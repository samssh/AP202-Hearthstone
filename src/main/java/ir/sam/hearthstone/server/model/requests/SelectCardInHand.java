package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectCardInHand extends Request {
    @Getter
    @Setter
    private int side, index;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectCardInHand(side, index);
    }
}