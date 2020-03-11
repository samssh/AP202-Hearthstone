package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Minion extends Cart {
    @Column
    private int attFrz;
    @Column
    private int hpFrz;



    // only hibernate use this constructor
    public Minion(){}

    public int getAttFrz() {
        return attFrz;
    }

    public void setAttFrz(int attFrz) {
        this.attFrz = attFrz;
    }

    public int getHpFrz() {
        return hpFrz;
    }

    public void setHpFrz(int hpFrz) {
        this.hpFrz = hpFrz;
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
