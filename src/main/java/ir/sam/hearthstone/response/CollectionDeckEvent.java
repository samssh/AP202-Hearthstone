package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.view.model.SmallDeckOverview;

public class CollectionDeckEvent extends Response{
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
    public void execute(Client client) {
        client.putCollectionDeckEvent(type,deckName,newDeck);
    }

    @Override
    public void accept(ResponseLogInfoVisitor responseLogInfoVisitor) {

    }
}
