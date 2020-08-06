package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class BuyCard extends Request {
    @Getter
    @Setter
    private String cardName;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.buyCard(cardName);
    }
}