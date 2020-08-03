package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.main.Weapon;
import lombok.Getter;
import lombok.Setter;

public class CardOverview extends UnitOverview {
    @Getter
    @Setter
    private int number;
    private final int price, mana;
    protected final int att, hp;
    private final boolean showPrice;

    public CardOverview(Card card) {
        this(card, 1, false);
    }

    public CardOverview(Card card, int number, boolean showPrice) {
        super(card.getName(), card.getName(), "class of card: " + card.getClassOfCard().getHeroName());
        this.number = number;
        this.price = card.getPrice();
        this.showPrice = showPrice;
        this.mana = card.getManaFrz();
        if (card instanceof Minion) {
            this.att = ((Minion) card).getAttFrz();
            this.hp = ((Minion) card).getHpFrz();
        } else if (card instanceof Weapon) {
            this.att = ((Weapon) card).getAttFrz();
            this.hp = ((Weapon) card).getUsage();
        } else {
            this.att = -1;
            this.hp = -1;
        }
    }

    public CardOverview(String name, String imageName, String toolkit, int number, int price, int mana, int att,
                        int hp, boolean showPrice) {
        super(name, imageName, toolkit);
        this.number = number;
        this.price = price;
        this.mana = mana;
        this.att = att;
        this.hp = hp;
        this.showPrice = showPrice;
    }

    @Override
    public String toString() {
        return "CardOverview{" +
                "number=" + number +
                ", price=" + price +
                ", mana=" + mana +
                ", att=" + att +
                ", hp=" + hp +
                ", showPrice=" + showPrice +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
