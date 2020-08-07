package ir.sam.hearthstone.server.model.main;


import javax.persistence.Entity;

@Entity
public class Spell extends Card {
    public Spell() {
    }

    @Override
    public String toString() {
        return "Spell{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + mana +
                ", price=" + price +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                '}';
    }

    @Override
    public Spell clone() {
        return (Spell) super.clone();
    }
}
