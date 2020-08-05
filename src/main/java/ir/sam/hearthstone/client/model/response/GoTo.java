package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

public class GoTo extends Response {
    @Getter
    @Setter
    private String panel, message;

    public GoTo(String panel, String message) {
        this.panel = panel;
        this.message = message;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.goTo(panel, message);
    }
}