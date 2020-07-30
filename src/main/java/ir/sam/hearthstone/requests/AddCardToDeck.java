package ir.sam.hearthstone.requests;

import lombok.Getter;

public class AddCardToDeck extends Request {
    @Getter
    private final String cardName, deckName;

    public AddCardToDeck(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.addCardToDeck(cardName, deckName);
    }
}