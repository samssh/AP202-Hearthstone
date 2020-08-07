package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class StartPlaying extends Request {
    @Getter
    @Setter
    private String mode;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.startPlay(mode);
    }
}