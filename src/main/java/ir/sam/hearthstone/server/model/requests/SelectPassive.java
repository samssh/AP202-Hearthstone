package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectPassive extends Request {
    @Getter
    @Setter
    private String passiveName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectPassive(passiveName);
    }
}