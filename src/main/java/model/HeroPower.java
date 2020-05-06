package model;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ToString
public class HeroPower implements SaveAble {
    @Id
    @Getter
    @Setter
    String name;
    @Column
    @Getter
    @Setter
    String description;
    @Column
    @Getter
    @Setter
    private int manaFrz;


    public HeroPower() {
    }

    public HeroPower(String name, String description, int manaFrz) {
        this.name = name;
        this.description = description;
        this.manaFrz = manaFrz;
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
}
