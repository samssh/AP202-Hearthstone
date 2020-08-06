package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectHero extends Request {
    @Getter
    @Setter
    private int side;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectHero(side);
    }
}
