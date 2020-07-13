package ir.sam.hearthstone.model.main;


import javax.persistence.Entity;

@Entity
public class Spell extends Card {
    public Spell() {
    }

    public Spell(String name, String description,
                 int price, ClassOfCard classOfCard,
                 Rarity rarity, int manaFrz) {
        super(name, description, price, classOfCard, rarity, manaFrz);
    }

    @Override
    public String toString() {
        return "Spell{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + manaFrz +
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
