package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

public class ShowMessage extends Response {
    @Getter
    @Setter
    private String message;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.showMessage(message);
    }
}