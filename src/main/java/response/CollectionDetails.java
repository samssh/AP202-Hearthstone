package response;

import client.Client;
import lombok.Getter;
import view.model.CardOverview;
import view.model.SmallDeckOverview;

import java.util.List;

public class CollectionDetails extends Response {
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

    public CollectionDetails(List<CardOverview> cards, List<SmallDeckOverview> decks,
                             List<CardOverview> deckCards, boolean canAddDeck,
                             boolean canChangeHero, String deckName) {
        this.cards = cards;
        this.decks = decks;
        this.deckCards = deckCards;
        this.canAddDeck = canAddDeck;
        this.canChangeHero = canChangeHero;
        this.deckName = deckName;
    }

    @Override
    public void execute(Client client) {
        client.setCollectionDetail(cards, decks, deckCards, canAddDeck, canChangeHero, deckName);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setCollectionDetailsInfo(this);
    }
}