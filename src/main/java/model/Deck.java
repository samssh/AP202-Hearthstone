package model;


import hibernate.Connector;
import hibernate.ManualMapping;
import hibernate.SaveAble;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @ManyToOne
    private Hero hero;
    @Transient
    private List<Card> cardList;
    @ElementCollection
    private List<String> cardListId;

    {
        cardListId = new ArrayList<>();
        cardList = new ArrayList<>();
    }
    // only hibernate use this constructor
    public Deck(){}

    public Deck(Hero hero){
        this.hero=hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void addCard(Card card){
        if (cardList.contains(card)) cardList.add(this.cardList.lastIndexOf(card),card);
        else cardList.add(card);
    }

    public int numberOfCard(Card card){
        int c=0;
        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).equals(card)){
                c++;
            }
        }
        return c;
    }

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
        ManualMapping.saveOrUpdateList(cardListId, cardList);
        connector.saveOrUpdate(this);

    }

    @Override
    public void load() {
        setCardList(ManualMapping.fetchList(Card.class, cardListId));
    }
}
