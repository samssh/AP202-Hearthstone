package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import lombok.Getter;
import lombok.Setter;

public class CollectionDeckEvent extends Response {
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private SmallDeckOverview newDeck;
    @Getter
    @Setter
    private String deckName;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.putCollectionDeckEvent(type, deckName, newDeck);
    }
}
