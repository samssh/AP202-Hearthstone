package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import lombok.Getter;

public class RemoveCardFromDeck extends Request {
    @Getter
    private final String cardName, deckName;

    public RemoveCardFromDeck(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public void execute(Server server) {
        server.removeCardFromDeck(cardName, deckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setRemoveCardFromDeck(this);
    }
}