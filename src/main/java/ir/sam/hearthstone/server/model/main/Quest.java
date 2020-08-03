package ir.sam.hearthstone.server.model.main;

import javax.persistence.Entity;

@Entity
public class Quest extends Spell {
    public Quest(String name, String description, int price, ClassOfCard classOfCard, Rarity rarity, int manaFrz) {
        super(name, description, price, classOfCard, rarity, manaFrz);
    }

    public Quest() {
    }

    @Override
    public Quest clone() {
        return (Quest) super.clone();
    }
}
