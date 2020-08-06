package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectPlayMode extends Request {
    @Getter
    @Setter
    private String modeName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectPlayMode(modeName);
    }
}
