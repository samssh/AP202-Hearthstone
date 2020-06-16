package ir.SAM.hearthstone.requests;

import lombok.Getter;
import ir.SAM.hearthstone.server.Server;

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