package model;

import hibernate.Connector;

import javax.persistence.*;

@Entity
public class Hero extends Unit {
    @Column
    private int hpFrz;
    // only hibernate use this constructor
    public Hero(){}

    public Hero(String name,String description,int price,int hpFrz){
        super(name,description,price);
        this.hpFrz=hpFrz;
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
        return "Hero{" +
                "hpFrz=" + hpFrz +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
