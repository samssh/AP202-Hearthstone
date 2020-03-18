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
    @Column
    int price;
    // only hibernate use this constructor
    public Unit(){}

    public Unit(String name,String description,int price){
        this.name=name;
        this.description=description;
        this.price=price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return Objects.equals(getName(), unit.getName());
    }
}
