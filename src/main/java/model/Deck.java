package model;


import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck implements SaveAble{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private long Id;
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @Setter
    @Getter
    private Hero hero;
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter
    @Getter
    private List<Card> cardList;

    {
        cardList = new ArrayList<>();
    }

    public Deck() {
    }

    Deck(Hero hero) {
        this.hero = hero;
    }

    public void addCard(Card card) {
        if (cardList.contains(card)) cardList.add(this.cardList.lastIndexOf(card), card);
        else cardList.add(card);
    }

    public void removeCard(Card card) {
        cardList.remove(card);
    }

    public int numberOfCard(Card card) {
        int c = 0;
        for (Card value : cardList) {
            if (value.equals(card)) {
                c++;
            }
        }
        return c;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void delete(Connector connector) {
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate(Connector connector) {
        connector.saveOrUpdate(this);
    }

    @Override
    public void load(Connector connector) {
    }

    public Deck getclone() {
        Deck deck=new Deck(this.hero);
        deck.getCardList().addAll(cardList);
        return deck;
    }
}
