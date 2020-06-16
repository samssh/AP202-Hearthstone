package ir.SAM.hearthstone.response;

import ir.SAM.hearthstone.client.Client;
import lombok.Getter;

public class ShowMessage extends Response {
    @Getter
    private final String message;

    public ShowMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute(Client client) {
        client.showMessage(message);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setShowMessageInfo(this);
    }
}