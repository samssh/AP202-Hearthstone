package ir.sam.hearthstone.model.main;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Weapon extends Card {
    @Column
    @Setter
    @Getter
    private int attFrz;
    @Column
    @Setter
    @Getter
    private int usage;

    public Weapon() {
    }


    public Weapon(String name, String description, int price,
                  ClassOfCard classOfCard, Rarity rarity,
                  int manaFrz, int attFrz, int usage) {
        super(name, description, price, classOfCard, rarity, manaFrz);
        this.attFrz = attFrz;
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", att=" + attFrz +
                ", usage=" + usage +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                ", manaFrz=" + manaFrz +
                ", price=" + price +
                '}';
    }
}
