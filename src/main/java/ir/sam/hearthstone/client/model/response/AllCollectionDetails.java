package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class AllCollectionDetails extends Response {
    @Getter
    @Setter
    private List<CardOverview> cards;
    @Getter
    @Setter
    private List<SmallDeckOverview> decks;
    @Getter
    @Setter
    private List<CardOverview> deckCards;
    @Getter
    @Setter
    private boolean canAddDeck, canChangeHero;
    @Getter
    @Setter
    private String deckName;
    @Getter
    @Setter
    private List<String> heroNames, classOfCardNames;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setCollectionDetail(cards, decks, deckCards, canAddDeck, canChangeHero,
                deckName, heroNames, classOfCardNames);
    }
}