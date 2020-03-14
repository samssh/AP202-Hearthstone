package model;

import javax.persistence.*;

@Entity
abstract public class Card extends Unit {
    @ManyToOne
    private ClassOfCard classOfCard;
    @Column
    private Rarity rarity;
    @Column
    private int manaFrz;

    // only hibernate use this constructor
    public Card() {}

    public Card(String name,String description,
                ClassOfCard classOfCard,
                Rarity rarity,int manaFrz) {
        super(name,description);
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

    @Override
    public boolean equals(Object o){
        if (o instanceof Card){
            try {
                if (this.getName().equals(((Card) o).getName()))
                    return true;
            }catch (NullPointerException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() +
                ",classOfCard=" + classOfCard +
                ", rarity=" + rarity +
                ", manaFrz=" + manaFrz;
    }
}
