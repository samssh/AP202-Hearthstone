package response;

import client.Client;
import lombok.Getter;

public class LoginResponse extends Response {
    @Getter
    private final boolean success;
    @Getter
    private final String message;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public void execute() {
        Client.getInstance().login(success, message);
    }
}