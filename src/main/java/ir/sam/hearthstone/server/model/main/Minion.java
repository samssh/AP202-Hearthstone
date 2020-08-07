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
    private int attack;
    @Column
    @Setter
    @Getter
    private int hp;

    public Minion() {
    }


    public Minion(String name, String description, int price,
                  ClassOfCard classOfCard, Rarity rarity,
                  int manaFrz, int attack, int hp) {
        super(name, description, price, classOfCard, rarity, manaFrz);
        this.attack = attack;
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "Minion{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + mana +
                ", price=" + price +
                ", att=" + attack +
                ", hp=" + hp +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                '}';
    }

    @Override
    public Minion clone() {
        return (Minion) super.clone();
    }
}
