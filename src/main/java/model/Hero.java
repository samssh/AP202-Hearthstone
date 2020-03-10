package model;

import javax.persistence.*;

@Entity
public class Hero extends Unit {


    // only hibernate use this constructor
    public Hero(){}
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
