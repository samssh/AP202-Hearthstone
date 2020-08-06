package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.main.Card;

public class CardOverview extends AbstractCardOverview {
    public CardOverview(Card card) {
        super(card);
    }

    public CardOverview(Card card, int number, boolean showPrice) {
        super(card, number, showPrice);
    }

    public CardOverview(String name, String imageName, String toolkit, int number, int price, int mana, int att, int hp, boolean showPrice) {
        super(name, imageName, toolkit, number, price, mana, att, hp, showPrice);
    }
}
