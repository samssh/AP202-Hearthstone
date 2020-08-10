package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

public class AddCardToDeck extends Request {
    @Getter
    @Setter
    private String cardName, deckName;

    @Override
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.addCardToDeck(cardName, deckName);
    }
}