package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class NewDeck extends Request {
    @Getter
    @Setter
    private String deckName, heroName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.newDeck(deckName, heroName);
    }
}