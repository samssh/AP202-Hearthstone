package model;


import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@ToString
public class Deck implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private long Id;
    @Column
    @Getter
    @Setter
    private String name;
    @Column
    @Setter
    @Getter
    private int wins;
    @Column
    @Setter
    @Getter
    private int games;
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @Setter
    @Getter
    private Hero hero;
    @ElementCollection
    @Cascade(CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter
    @Getter
    private Map<Card, CardDetails> cards;

    {
        cards = new HashMap<>();
    }

    public Deck() {
    }

    public Deck(Hero hero,String name) {
        this.hero = hero;
        this.name = name;
    }

    public void addCard(Card card) {
        if (cards.containsKey(card)) cards.get(card).vRepeatedTimes(1);
        else cards.put(card, new CardDetails(1));
    }

    public double getWinRate(){
        return games != 0 ? (wins + 0.0) / games : -1;
    }


    public double getManaAverage(){
        double sum=0,n=0;
        for (Card card:cards.keySet()) {
            n++;
            sum+=card.manaFrz;
        }

        return n != 0 ? (sum) / n : -1;
    }

    public void removeCard(Card card) {
        if (cards.containsKey(card)) {
            cards.get(card).vRepeatedTimes(-1);
            if (cards.get(card).getRepeatedTimes() == 0) cards.remove(card);
        }
    }

    public int numberOfCard(Card card) {
        if (cards.containsKey(card))
            return cards.get(card).getRepeatedTimes();
        return 0;
    }

    public int getSize(){
        return cards.keySet().stream().mapToInt(card -> cards.get(card).getRepeatedTimes()).sum();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Objects.equals(name, deck.name);
    }
}
