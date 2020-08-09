package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.account.Deck;

public class SmallDeckOverview extends Overview {
    public SmallDeckOverview(Deck deck) {
        super(deck.getName(), deck.getHero().getName());
    }

    @Override
    public String toString() {
        return "SmallDeckOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
