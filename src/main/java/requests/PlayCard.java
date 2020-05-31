package requests;

import lombok.Getter;
import server.Server;

public class PlayCard extends Request {
    @Getter
    private final String cardName;

    public PlayCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute() {
        Server.getInstance().playCard(cardName);
    }
}