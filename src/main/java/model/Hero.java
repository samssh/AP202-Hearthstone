package model;

import hibernate.Connector;

import javax.persistence.*;

@Entity
public class Hero extends Unit {
    @Column
    private int hpFrz;
    // only hibernate use this constructor
    public Hero(){}

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
}
