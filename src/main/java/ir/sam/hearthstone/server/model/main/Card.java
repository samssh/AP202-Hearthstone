package ir.sam.hearthstone.server.model.main;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
abstract public class Card extends Unit {
    @ManyToOne
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
    protected int manaFrz;
    @Column
    @Setter
    @Getter
    protected int price;

    public Card() {
    }

    Card(String name, String description, int price,
         ClassOfCard classOfCard,
         Rarity rarity, int manaFrz) {
        super(name, description);
        this.classOfCard = classOfCard;
        this.rarity = rarity;
        this.manaFrz = manaFrz;
        this.price = price;
    }

    public static int getInstanceValue(Card card) {
        if (card instanceof Minion) return 4;
        else if (card instanceof Spell) return 3;
        else return -1;
    }
}
