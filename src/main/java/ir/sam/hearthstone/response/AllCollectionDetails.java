package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.view.model.CardOverview;
import ir.sam.hearthstone.client.view.model.SmallDeckOverview;
import lombok.Getter;

import java.util.List;

public class AllCollectionDetails extends Response {
    @Getter
    private final List<CardOverview> cards;
    @Getter
    private final List<SmallDeckOverview> decks;
    @Getter
    private final List<CardOverview> deckCards;
    @Getter
    private final boolean canAddDeck, canChangeHero;
    @Getter
    private final String deckName;
    @Getter
    private final List<String> heroNames, classOfCardNames;

    public AllCollectionDetails(List<CardOverview> cards, List<SmallDeckOverview> decks,
                                List<CardOverview> deckCards, boolean canAddDeck,
                                boolean canChangeHero, String deckName, List<String> heroNames, List<String> classOfCardNames) {
        this.cards = cards;
        this.decks = decks;
        this.deckCards = deckCards;
        this.canAddDeck = canAddDeck;
        this.canChangeHero = canChangeHero;
        this.deckName = deckName;
        this.heroNames = heroNames;
        this.classOfCardNames = classOfCardNames;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setCollectionDetail(cards, decks, deckCards, canAddDeck, canChangeHero,
                deckName, heroNames, classOfCardNames);
    }
}