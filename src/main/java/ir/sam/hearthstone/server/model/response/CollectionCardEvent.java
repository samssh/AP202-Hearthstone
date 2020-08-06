package ir.sam.hearthstone.server.model.response;

import lombok.Getter;

public class CollectionCardEvent extends Response {
    @Getter
    private final String type;
    @Getter
    private final String cardName;
    @Getter
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