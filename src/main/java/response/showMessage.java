package response;

import client.Client;
import lombok.Getter;

public class showMessage extends Response {
    @Getter
    private final String message;

    public showMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        Client.getInstance().showMessage(message);
    }
}