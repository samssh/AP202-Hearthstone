package model;

import javax.persistence.*;

@Entity
abstract public class Cart extends Unit {



    // only hibernate use this constructor
    public Cart() {
    }
    @Column
    @ManyToOne
    private ClassOfCart classOfCart;

    public ClassOfCart getClassOfCart() {
        return classOfCart;
    }

    public void setClassOfCart(ClassOfCart classOfCart) {
        this.classOfCart = classOfCart;
    }



}
