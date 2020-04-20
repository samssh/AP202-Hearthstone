package model;

import hibernate.Connector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @SuppressWarnings("unchecked")
    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String toString() {
        return "Minion{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + manaFrz +
                ", price=" + price +
                "att=" + attFrz +
                ", hp=" + hpFrz +
                ", classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                '}';
    }
}
