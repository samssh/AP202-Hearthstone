package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

public class CollectionCardEvent extends Response {
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String cardName;
    @Getter
    @Setter
    private boolean canAddDeck, canChangeHero;

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