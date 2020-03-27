package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
abstract public class Card extends Unit {
    @ManyToOne
    @Setter
    @Getter
    ClassOfCard classOfCard;
    @Column
    @Setter
    @Getter
    Rarity rarity;
    @Column
    @Setter
    @Getter
    int manaFrz;
    @Column
    @Setter
    @Getter
    int price;

    // only hibernate use this constructor
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
}
