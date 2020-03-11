package model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Spell extends Cart {

    // only hibernate use this constructor
    public Spell() {
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

    @Override
    public void load() {

    }

    @Override
    public Serializable getId() {
        return null;
    }
}
