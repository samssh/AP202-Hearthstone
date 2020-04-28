package view.model;

import lombok.Getter;
import model.Card;

public class CardOverview {
    public final static int NORMAL = 0, GRAY = 1;
    @Getter
    private final String name,classOfCard;
    @Getter
    private final int number, colorType, price;
    @Getter
    private final boolean showPrice;

    public CardOverview(Card card, int number, int colorType,boolean showPrice) {
        this.name = card.getName();
        this.number = number;
        this.colorType = colorType;
        this.price = card.getPrice();
        this.classOfCard = card.getClassOfCard().getHeroName();
        this.showPrice = showPrice;
    }
}
