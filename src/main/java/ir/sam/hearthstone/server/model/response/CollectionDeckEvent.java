package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.SmallDeckOverview;
import lombok.Getter;

public class CollectionDeckEvent extends Response {
    @Getter
    private final String type;
    @Getter
    private final SmallDeckOverview newDeck;
    @Getter
    private final String deckName;

    public CollectionDeckEvent(String type, SmallDeckOverview newDeck) {
        this(type, newDeck, null);
    }

    public CollectionDeckEvent(String type, SmallDeckOverview newDeck, String oldDeckName) {
        this.type = type;
        this.newDeck = newDeck;
        this.deckName = oldDeckName;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.putCollectionDeckEvent(type, deckName, newDeck);
    }
}
