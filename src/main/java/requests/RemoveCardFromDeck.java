package requests;

import lombok.Getter;
import server.Server;

public class RemoveCardFromDeck extends Request {
    @Getter
    private final String cardName, deckName;

    public RemoveCardFromDeck(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public void execute() {
        Server.getInstance().removeCardFromDeck(cardName, deckName);
    }
}