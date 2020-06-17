package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import lombok.Getter;

public class SellCard extends Request {
    @Getter
    private final String cardName;

    public SellCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void execute(Server server) {
        server.sellCard(cardName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setSellCard(this);
    }
}