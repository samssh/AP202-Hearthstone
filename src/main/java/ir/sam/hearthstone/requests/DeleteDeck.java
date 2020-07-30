package ir.sam.hearthstone.requests;

import lombok.Getter;

public class DeleteDeck extends Request {
    @Getter
    private final String deckName;

    public DeleteDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.deleteDeck(deckName);
    }
}