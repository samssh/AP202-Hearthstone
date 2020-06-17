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
abstract public class Unit implements SaveAble {
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
}
