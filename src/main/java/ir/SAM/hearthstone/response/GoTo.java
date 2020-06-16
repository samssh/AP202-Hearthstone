package ir.SAM.hearthstone.response;

import ir.SAM.hearthstone.client.Client;
import lombok.Getter;

public class GoTo extends Response {
    @Getter
    private final String panel, message;

    public GoTo(String panel, String message) {
        this.panel = panel;
        this.message = message;
    }


    @Override
    public void execute(Client client) {
        client.goTo(panel, message);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setGoToInfo(this);
    }
}