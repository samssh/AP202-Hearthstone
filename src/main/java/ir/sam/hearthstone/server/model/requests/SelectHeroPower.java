package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectHeroPower extends Request {
    @Getter
    @Setter
    private int side;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectHeroPower(side);
    }
}
