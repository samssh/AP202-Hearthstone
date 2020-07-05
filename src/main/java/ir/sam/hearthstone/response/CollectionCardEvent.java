package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;

public class CollectionCardEvent extends Response {
    private final String type;
    private final String cardName;
    private final boolean canAddDeck, canChangeHero;

    public CollectionCardEvent(String type, String cardName, boolean canAddDeck, boolean canChangeHero) {
        this.type = type;
        this.cardName = cardName;
        this.canAddDeck = canAddDeck;
        this.canChangeHero = canChangeHero;
    }

    @Override
    public void execute(Client client) {
        client.putCollectionCardEvent(type,cardName,canAddDeck,canChangeHero);
    }

    @Override
    public void accept(ResponseLogInfoVisitor responseLogInfoVisitor) {
    }
}