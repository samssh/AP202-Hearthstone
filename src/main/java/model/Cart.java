package model;

import javax.persistence.*;

@Entity
abstract public class Cart extends Unit {
    @Column
    String heroName;
    // only hibernate use this constructor
    public Cart(){}

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }
}
