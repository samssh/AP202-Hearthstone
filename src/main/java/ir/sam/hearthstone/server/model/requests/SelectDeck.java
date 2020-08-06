package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SelectDeck extends Request {
    @Getter
    @Setter
    private String deckName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectDeck(deckName);
    }
}