package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.account.Deck;
import lombok.ToString;

@ToString(includeFieldNames = false)
public class SmallDeckOverview extends Overview {
    public SmallDeckOverview(Deck deck) {
        super(deck.getName(), deck.getHero().getName());
    }
}
