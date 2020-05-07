package model.main;

import hibernate.Connector;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class  Hero extends Unit {
    @Column
    @Setter
    @Getter
    private int hpFrz;
    @OneToOne
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
    @Setter
    @Getter
    private HeroPower power;

    public Hero() {
    }

    public Hero(String name, String description, int hpFrz,HeroPower power) {
        super(name, description);
        this.hpFrz = hpFrz;
        this.power = power;
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
    public void load(Connector connector) {}

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hp=" + hpFrz +
                ", power=" + power +
                '}';
    }
}
