package response;

import client.Client;
import lombok.Getter;
import util.ResponseLogInfoVisitor;

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