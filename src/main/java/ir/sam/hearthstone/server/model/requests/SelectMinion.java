package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

public class SelectMinion extends Request {
    @Getter
    @Setter
    private int side, index, emptyIndex;

    @Override
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.selectMinion(side, index, emptyIndex);
    }
}
