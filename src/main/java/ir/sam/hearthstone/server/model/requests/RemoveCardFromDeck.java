package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class RemoveCardFromDeck extends Request {
    @Getter
    @Setter
    private String cardName, deckName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.removeCardFromDeck(cardName, deckName);
    }
}