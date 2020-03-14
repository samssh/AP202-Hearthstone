package model;

import hibernate.SaveAble;

import javax.persistence.*;

@Entity
abstract public class Unit implements SaveAble {
    @Id
    private String name;
    @Column
    private String description;
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
    public String toString() {
        return "card{" +
                "name=" + name;
//                ", description=" + description;
    }
}
