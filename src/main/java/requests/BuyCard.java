package requests;

import lombok.Getter;
import server.Server;

public class BuyCard extends Request {
    @Getter
    private final String cardName;

    public BuyCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute() {
        Server.getInstance().buyCard(cardName);
    }
}