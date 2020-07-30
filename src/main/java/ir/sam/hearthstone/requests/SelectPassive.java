package ir.sam.hearthstone.requests;

import lombok.Getter;

public class SelectPassive extends Request {
    @Getter
    private final String passiveName;

    public SelectPassive(String passiveName) {
        this.passiveName = passiveName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectPassive(passiveName);
    }
}