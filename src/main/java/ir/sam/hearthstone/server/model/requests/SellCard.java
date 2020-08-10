package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

public class SellCard extends Request {
    @Getter
    @Setter
    private String cardName;

    @Override
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.sellCard(cardName);
    }

}