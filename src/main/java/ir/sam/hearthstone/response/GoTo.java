package ir.sam.hearthstone.response;

import lombok.Getter;

public class GoTo extends Response {
    @Getter
    private final String panel, message;

    public GoTo(String panel, String message) {
        this.panel = panel;
        this.message = message;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.goTo(panel, message);
    }
}