package ir.SAM.hearthstone.requests;

import lombok.Getter;
import ir.SAM.hearthstone.server.Server;

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