package ir.sam.hearthstone.requests;

import lombok.Getter;

public class RemoveCardFromDeck extends Request {
    @Getter
    private final String cardName, deckName;

    public RemoveCardFromDeck(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.removeCardFromDeck(cardName, deckName);
    }
}