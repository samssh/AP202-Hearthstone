package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.CardOverview;
import lombok.Getter;

import java.util.List;

public class CollectionAllCards extends Response {
    @Getter
    private final List<CardOverview> cards;
    @Getter
    private final List<CardOverview> deckCards;
    @Getter
    private final boolean canAddDeck, canChangeHero;
    @Getter
    private final String deckName;

    public CollectionAllCards(List<CardOverview> cards, List<CardOverview> deckCards,
                              boolean canAddDeck, boolean canChangeHero, String deckName) {
        this.cards = cards;
        this.deckCards = deckCards;
        this.canAddDeck = canAddDeck;
        this.canChangeHero = canChangeHero;
        this.deckName = deckName;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setCollectionDetail(cards, null, deckCards, canAddDeck, canChangeHero, deckName,
                null, null);
    }
}
