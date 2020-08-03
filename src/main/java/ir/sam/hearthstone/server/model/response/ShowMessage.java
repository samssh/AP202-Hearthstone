package ir.sam.hearthstone.server.model.response;

import lombok.Getter;

public class ShowMessage extends Response {
    @Getter
    private final String message;

    public ShowMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.showMessage(message);
    }
}