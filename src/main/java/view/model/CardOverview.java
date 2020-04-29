package view.model;

import lombok.Getter;
import model.Card;

public class CardOverview {
    @Getter
    private final String name,classOfCard;
    @Getter
    private final int number, price;
    @Getter
    private final boolean showPrice;

    public CardOverview(Card card, int number,boolean showPrice) {
        this.name = card.getName();
        this.number = number;
        this.price = card.getPrice();
        this.classOfCard = card.getClassOfCard().getHeroName();
        this.showPrice = showPrice;
    }
}
