package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import lombok.Getter;

public class DeleteDeck extends Request {
    @Getter
    private final String deckName;

    public DeleteDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(Server server) {
        server.deleteDeck(deckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setDeleteDeck(this);
    }
}