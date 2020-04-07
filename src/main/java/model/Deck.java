package model;


import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private long Id;
    @ManyToOne
    @Setter
    @Getter
    private Hero hero;
    @Transient
    @Setter
    @Getter
    private List<Card> cardList;
    @ElementCollection
    @Setter
    @Getter
    private List<String> cardListId;

    {
        cardListId = new ArrayList<>();
        cardList = new ArrayList<>();
    }

    // only hibernate use this constructor
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
    public void delete() {
        Connector connector = Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        connector.saveOrUpdateList(cardListId, cardList);
        connector.saveOrUpdate(this);

    }

    @Override
    public void load() {
        Connector connector = Connector.getConnector();
        connector.fetchList(Card.class, cardListId, cardList);
    }

    @Override
    public Deck clone() {
        Deck deck=new Deck(this.hero);
        deck.getCardList().addAll(cardList);
        return deck;
    }
}
