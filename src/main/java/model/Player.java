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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
public class Player implements SaveAble {
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
    @Column
    @Getter
    @Setter
    private int selectedDeckIndex;
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter
    @Getter
    @JoinTable(name = "Player_Card")
    private List<Card> cards;
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter
    @Getter
    @JoinTable(name = "Player_Hero")
    private List<Hero> heroes;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    @Setter
    @Getter
    private List<Deck> decks;

    {
        cards = new ArrayList<>();
        heroes = new ArrayList<>();
        decks = new ArrayList<>();
    }

    public Player() {
    }

    public Player(String userName, String password, long creatTime,
                  int coin, int selectedDeckIndex,
                  List<Card> cards, List<Hero> heroes, List<Deck> decks) {
        this.userName = userName;
        this.password = password;
        this.creatTime = creatTime;
        this.coin = coin;
        this.selectedDeckIndex = selectedDeckIndex;
        this.cards = cards;
        this.heroes = heroes;
        this.decks = decks;
    }

    public void addCard(Card card) {
        if (cards.contains(card)) cards.add(this.cards.lastIndexOf(card), card);
        else cards.add(card);
    }

    public void removeCard(Card card) {
        int number = numberOfCard(card);
        cards.remove(card);
        for (Deck deck : decks) {
            deck.removeCard(card, number);
        }
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

    public int getHeroDeckIndex(Hero h) {
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getHero().getName().equals(h.getName()))
                return i;

        }
        System.err.println("problem in getHeroDeck");
        return -1;
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

    @SuppressWarnings("unchecked")
    @Override
    public String getId() {
        return getUserName();
    }

    public Deck getSelectedDeck() {
        return decks.get(selectedDeckIndex);
    }

    public Hero getSelectedHero() {
        return decks.get(selectedDeckIndex).getHero();
    }
}
