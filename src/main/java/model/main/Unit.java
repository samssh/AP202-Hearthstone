package model.main;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
abstract public class Unit implements SaveAble {
    @Id
    @Setter
    @Getter
    String name;
    @Setter
    @Getter
    @Column
    String description;

    public Unit() {
    }

    Unit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return Objects.equals(getName(), unit.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
