package requests;

import lombok.Getter;
import server.Server;

public class SellCard extends Request {
    @Getter
    private final String cardName;

    public SellCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute() {
        Server.getInstance().sellCard(cardName);
    }
}