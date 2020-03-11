package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Hero extends Unit {


    // only hibernate use this constructor
    public Hero(){}

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
