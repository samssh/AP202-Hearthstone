package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectCardOnPassive extends Request {
    @Getter
    @Setter
    private int index;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectCadOnPassive(index);
    }
}
