package requests;

import lombok.Getter;
import server.Server;

public class AddCardToDeck extends Request {
    @Getter
    private final String cardName, deckName;

    public AddCardToDeck(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public void execute() {
        Server.getInstance().addCardToDeck(cardName, deckName);
    }
}