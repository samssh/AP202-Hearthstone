package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectMinion extends Request {
    @Getter
    @Setter
    private int side, index, emptyIndex;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectMinion(side, index, emptyIndex);
    }
}
