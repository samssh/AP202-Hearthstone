package model;

import hibernate.SaveAble;

import javax.persistence.*;

@Entity
abstract public class Unit implements SaveAble {
    @Id
    String name;
    @Column
    String description;
    // only hibernate use this constructor
    public Unit(){}

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
}
