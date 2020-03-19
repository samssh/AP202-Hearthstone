package model;

import hibernate.SaveAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
abstract public class Unit implements SaveAble {
    @Id
    String name;
    @Column
    String description;
    // only hibernate use this constructor
    public Unit(){}

    public Unit(String name,String description){
        this.name=name;
        this.description=description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return Objects.equals(getName(), unit.getName());
    }
}
