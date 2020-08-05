package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

public class LoginResponse extends Response {
    @Getter
    @Setter
    private boolean success;
    @Getter
    @Setter
    private String message;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.login(success, message);
    }
}