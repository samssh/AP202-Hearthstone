package model;

import javax.persistence.*;

@Entity
abstract public class Card extends Unit {
    @ManyToOne
    ClassOfCard classOfCard;
    @Column
    Rarity rarity;
    @Column
    int manaFrz;

    // only hibernate use this constructor
    public Card() {}

    public Card(String name,String description,int price,
                ClassOfCard classOfCard,
                Rarity rarity,int manaFrz) {
        super(name,description,price);
        this.classOfCard=classOfCard;
        this.rarity=rarity;
        this.manaFrz=manaFrz;
    }


    public ClassOfCard getClassOfCard() {
        return classOfCard;
    }

    public void setClassOfCard(ClassOfCard classOfCard) {
        this.classOfCard = classOfCard;
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

}
