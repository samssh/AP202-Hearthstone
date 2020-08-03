package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.SmallDeckOverview;

public class CollectionDeckEvent extends Response {
    private final String type;
    private final SmallDeckOverview newDeck;
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
