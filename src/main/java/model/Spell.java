package model;


import javax.persistence.*;

@Entity
public class Spell extends Cart {

    // only hibernate use this constructor
    public Spell() {
    }

    @Override
    public Integer save() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void saveOrUpdate() {

    }
}
