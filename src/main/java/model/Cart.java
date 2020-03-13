package model;

import javax.persistence.*;

@Entity
abstract public class Cart extends Unit {
    @Column
    @ManyToOne
    private ClassOfCart classOfCart;
    @Column
    private Rarity rarity;
    @Column
    private int manaFrz;

    // only hibernate use this constructor
    public Cart() {
    }

    public ClassOfCart getClassOfCart() {
        return classOfCart;
    }

    public void setClassOfCart(ClassOfCart classOfCart) {
        this.classOfCart = classOfCart;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getManaFrz() {
        return manaFrz;
    }

    public void setManaFrz(int manaFrz) {
        this.manaFrz = manaFrz;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Cart){
            try {
                if (this.getName().equals(((Cart) o).getName()))
                    return true;
            }catch (NullPointerException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
