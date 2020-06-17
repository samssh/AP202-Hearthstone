package ir.sam.hearthstone.requests;

import lombok.Getter;
import ir.sam.hearthstone.server.Server;

public class BuyCard extends Request {
    @Getter
    private final String cardName;

    public BuyCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute(Server server) {
        server.buyCard(cardName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setBuyCard(this);
    }
}