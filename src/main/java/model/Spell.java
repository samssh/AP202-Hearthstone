package model;


import hibernate.Connector;

import javax.persistence.*;

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
    public void delete() {
        Connector connector = Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        connector.saveOrUpdate(this);
    }

    @Override
    public void load() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String toString() {
        return "Spell{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + manaFrz +
                ", price=" + price +
                "classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                '}';
    }
}
