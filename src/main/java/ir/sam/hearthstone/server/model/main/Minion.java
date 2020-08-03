package ir.sam.hearthstone.server.model.main;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Minion extends Card {
    @Column
    @Setter
    @Getter
    private int attFrz;
    @Column
    @Setter
    @Getter
    private int hpFrz;

    public Minion() {
    }


    public Minion(String name, String description, int price,
                  ClassOfCard classOfCard, Rarity rarity,
                  int manaFrz, int attFrz, int hpFrz) {
        super(name, description, price, classOfCard, rarity, manaFrz);
        this.attFrz = attFrz;
        this.hpFrz = hpFrz;
    }

    @Override
    public String toString() {
        return "Minion{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + manaFrz +
                ", price=" + price +
                ", att=" + attFrz +
                ", hp=" + hpFrz +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                '}';
    }

    @Override
    public Minion clone() {
        return (Minion) super.clone();
    }
}
