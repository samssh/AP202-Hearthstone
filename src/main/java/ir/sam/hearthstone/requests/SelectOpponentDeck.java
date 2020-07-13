package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class SelectOpponentDeck extends Request{
    private final String deckName;

    public SelectOpponentDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(Server server) {
        server.selectOpponentDeck(deckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
