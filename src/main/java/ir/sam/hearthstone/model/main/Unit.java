package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(of = "name")
public abstract class Unit implements SaveAble, Cloneable {
    @Id
    @Setter
    @Getter
    protected String name;
    @Setter
    @Getter
    @Column
    protected String description;

    public Unit() {
    }

    Unit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException ignore) {
            // this shouldn't happen, since we are Cloneable
        }
        return null;
    }
}
