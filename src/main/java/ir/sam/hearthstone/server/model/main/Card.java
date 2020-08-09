package ir.sam.hearthstone.server.model.main;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
abstract public class Card extends Unit {
    @ManyToOne()
    @JoinColumn(name = "class_of_card_hero_name")
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    protected ClassOfCard classOfCard;
    @Column
    @Setter
    @Getter
    protected Rarity rarity;
    @Column
    @Setter
    @Getter
    protected int mana;
    @Column
    @Setter
    @Getter
    protected int price;

    public Card() {
    }

    Card(String name, String description, int price,
         ClassOfCard classOfCard,
         Rarity rarity, int mana) {
        super(name, description);
        this.classOfCard = classOfCard;
        this.rarity = rarity;
        this.mana = mana;
        this.price = price;
    }

    public static int getInstanceValue(Card card) {
        if (card instanceof Minion) return 4;
        else if (card instanceof Weapon) return 3;
        else if (card instanceof Quest) return 1;
        else if (card instanceof Spell) return 2;
        else return -1;
    }
}
