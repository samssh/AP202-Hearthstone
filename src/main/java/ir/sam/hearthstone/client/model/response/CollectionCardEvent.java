package ir.sam.hearthstone.client.model.response;

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
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.putCollectionCardEvent(type, cardName, canAddDeck, canChangeHero);
    }
}