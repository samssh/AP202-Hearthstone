package model;

import hibernate.Connector;
import hibernate.SaveAble;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ClassOfCart implements SaveAble {
    @Id
    private String heroName;


    public ClassOfCart(){}

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }
    @Override
    public void update() {
        Connector connector=Connector.getConnector();
        connector.update(this);
    }

    @Override
    public void delete() {
        Connector connector=Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector=Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void load() {
    }

    @Override
    public Serializable getId() {
        return heroName;
    }
}
