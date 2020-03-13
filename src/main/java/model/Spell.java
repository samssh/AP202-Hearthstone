package model;


import hibernate.Connector;

import javax.persistence.*;

@Entity
public class Spell extends Cart {

    // only hibernate use this constructor
    public Spell() {
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
