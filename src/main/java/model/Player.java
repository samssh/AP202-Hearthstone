package model;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player implements SaveAble {
    // id nano time
    @Id
    @Setter
    @Getter
    private String userName;
    @Column
    @Setter
    @Getter
    private String password;
    @Column
    @Setter
    @Getter
    private long creatTime;
    @Column
    @Setter
    @Getter
    private int coin;
    @ManyToOne
    @Setter
    @Getter
    private Hero selectedHero;
    @ManyToOne
    @Setter
    @Getter
    private Deck selectedDeck;
    @ElementCollection
    @Setter
    @Getter
    private List<String> cardsId;
    @Transient
    @Setter
    @Getter
    private List<Card> cards;
    @ElementCollection
    @Setter
    @Getter
    private List<String> heroesId;
    @Transient
    @Setter
    @Getter
    private List<Hero> heroes;
    @ElementCollection
    @Setter
    @Getter
    private List<Long> decksId;
    @Transient
    @Setter
    @Getter
    private List<Deck> decks;

    {
        cardsId = new ArrayList<>();
        cards = new ArrayList<>();
        heroes = new ArrayList<>();
        heroesId = new ArrayList<>();
        decks = new ArrayList<>();
        decksId = new ArrayList<>();
    }

    // only hibernate use this constructor
    public Player() {
    }

    public Player(String userName, String password, Long creatTime,
                  int coin, Hero selectedHero, Deck selectedDeck,
                  List<Card> cards, List<Hero> heroes, List<Deck> decks) {
        this.userName = userName;
        this.password = password;
        this.creatTime = creatTime;
        this.coin = coin;
        this.selectedHero = selectedHero;
        this.selectedDeck = selectedDeck;
        this.cards = cards;
        this.heroes = heroes;
        this.decks = decks;
    }

    public void addCard(Card card) {
        if (cards.contains(card)) cards.add(this.cards.lastIndexOf(card), card);
        else cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public int numberOfCard(Card card) {
        int c = 0;
        for (Card value : cards) {
            if (value.equals(card)) {
                c++;
            }
        }
        return c;
    }

    public boolean isInDeck(Card card) {
        for (Deck d : decks)
            if (d.numberOfCard(card) > 0)
                return true;
        return false;
    }

    public Deck getHeroDeck(Hero h) {
        for (Deck d : decks) {
            if (d.getHero().getName().equals(h.getName()))
                return d;
        }
        System.err.println("problem in getHeroDeck");
        return null;

    }

    @Override
    public void delete() {
        Connector connector = Connector.getConnector();
        connector.deleteList(decks);
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        connector.saveOrUpdateList(cardsId, cards);
        connector.saveOrUpdateList(heroesId, heroes);
        connector.saveOrUpdateList(decksId, decks);
        connector.saveOrUpdate(this);
    }

    @Override
    public void load() {
        Connector connector=Connector.getConnector();
        connector.fetchList(Deck.class, decksId,decks);
        connector.fetchList(Hero.class, heroesId,heroes);
        connector.fetchList(Card.class, cardsId,cards);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getId() {
        return getUserName();
    }
}
