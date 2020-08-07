package ir.sam.hearthstone.server.model.main;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Weapon extends Card {
    @Column
    @Setter
    @Getter
    private int attack;
    @Column
    @Setter
    @Getter
    private int usage;

    public Weapon() {
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", att=" + attack +
                ", usage=" + usage +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                ", manaFrz=" + mana +
                ", price=" + price +
                '}';
    }

    @Override
    public Weapon clone() {
        return (Weapon) super.clone();
    }
}
