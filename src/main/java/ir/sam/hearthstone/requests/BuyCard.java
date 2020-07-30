package ir.sam.hearthstone.requests;

import lombok.Getter;

public class BuyCard extends Request {
    @Getter
    private final String cardName;

    public BuyCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.buyCard(cardName);
    }
}