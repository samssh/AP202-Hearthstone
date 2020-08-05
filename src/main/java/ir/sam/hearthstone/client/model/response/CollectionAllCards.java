package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CollectionAllCards extends Response {
    @Getter
    @Setter
    private List<CardOverview> cards;
    @Setter
    @Getter
    private List<CardOverview> deckCards;
    @Setter
    @Getter
    private boolean canAddDeck, canChangeHero;
    @Getter
    @Setter
    private String deckName;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setCollectionDetail(cards, null, deckCards, canAddDeck, canChangeHero
                , deckName, null, null);
    }
}
