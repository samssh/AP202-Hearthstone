package model;

import hibernate.Connector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class  Hero extends Unit {
    @Column
    @Setter
    @Getter
    private int hpFrz;

    public Hero() {
    }

    public Hero(String name, String description, int hpFrz) {
        super(name, description);
        this.hpFrz = hpFrz;
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
        return "Hero{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                "hp=" + hpFrz +
                '}';
    }
}
