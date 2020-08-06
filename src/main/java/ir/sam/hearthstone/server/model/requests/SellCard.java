package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class SellCard extends Request {
    @Getter
    @Setter
    private String cardName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sellCard(cardName);
    }

}