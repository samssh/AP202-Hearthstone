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
    public void delete(Connector connector) {
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate(Connector connector) {
        connector.saveOrUpdate(this);
    }

    @Override
    public void load(Connector connector) {
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
}
