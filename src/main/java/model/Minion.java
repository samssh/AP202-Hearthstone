package model;

import hibernate.Connector;

import javax.persistence.*;

@Entity
public class Minion extends Card {
    @Column
    private int attFrz;
    @Column
    private int hpFrz;

    // only hibernate use this constructor
    public Minion(){}

    public Minion(String name, String description,
                  ClassOfCard classOfCard, Rarity rarity,
                  int manaFrz, int attFrz, int hpFrz) {
        super(name, description, classOfCard, rarity, manaFrz);
        this.attFrz = attFrz;
        this.hpFrz = hpFrz;
    }

    public int getAttFrz() {
        return attFrz;
    }

    public void setAttFrz(int attFrz) {
        this.attFrz = attFrz;
    }

    public int getHpFrz() {
        return hpFrz;
    }

    public void setHpFrz(int hpFrz) {
        this.hpFrz = hpFrz;
    }

    @Override
    public void delete() {
        Connector connector=Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector=Connector.getConnector();
        connector.saveOrUpdate(this);

    }

    @Override
    public void load() {


    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String toString() {
        return super.toString() +
                "attFrz=" + attFrz +
                ", hpFrz=" + hpFrz +
                '}';
    }
}
