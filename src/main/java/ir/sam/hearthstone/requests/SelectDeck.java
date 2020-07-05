package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectDeck extends Request {
    private final String deckName;

    public SelectDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(Server server) {
        server.selectDeck(deckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}