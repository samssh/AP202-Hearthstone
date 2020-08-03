package ir.sam.hearthstone.client.model.requests;

import lombok.Getter;

public class NewDeck extends Request {
    @Getter
    private final String deckName, heroName;

    public NewDeck(String deckName, String heroName) {
        this.deckName = deckName;
        this.heroName = heroName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.newDeck(deckName, heroName);
    }
}