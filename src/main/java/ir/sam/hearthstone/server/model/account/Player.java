package ir.sam.hearthstone.server.model.account;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Hero;
import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.*;

@Entity
public class Player implements SaveAble {
    @Id
    @Setter
    @Getter
    private String username;
    @Column
    @Setter
    @Getter
    private String password;
    @Column(name = "creat_time")
    @Setter
    @Getter
    private long creatTime;
    @Column
    @Setter
    @Getter
    private int coin;
    @Column
    @Setter
    @Getter
    private int cup;
    @Column(name = "selected_deck_index")
    @Getter
    @Setter
    private int selectedDeckIndex;
    @ElementCollection
    @Setter
    @Getter
    @JoinTable(name = "player_card")
    private Map<Card, CardDetails> cards;
    @ManyToMany
    @Setter
    @Getter
    @JoinTable(name = "player_hero")
    private List<Hero> heroes;
    @OneToMany
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    private List<Deck> decks;

    {
        cards = new HashMap<>();
        heroes = new ArrayList<>();
        decks = new ArrayList<>();
    }

    public Player() {
    }

    public Player(String username, String password, long creatTime,
                  int coin, int selectedDeckIndex,
                  Map<Card, CardDetails> cards, List<Hero> heroes, List<Deck> decks) {
        this.username = username;
        this.password = password;
        this.creatTime = creatTime;
        this.coin = coin;
        this.selectedDeckIndex = selectedDeckIndex;
        this.cards = cards;
        this.heroes = heroes;
        this.decks = decks;
        decks.forEach(deck -> deck.setPlayer(this));
    }

    public void addCard(Card card) {
        if (cards.containsKey(card)) cards.get(card).vRepeatedTimes(1);
        else cards.put(card, new CardDetails(1));
    }

    public void removeCard(Card card) {
        cards.get(card).vRepeatedTimes(-1);
        if (cards.get(card).getRepeatedTimes() == 0) cards.remove(card);
        for (Deck deck : decks) deck.removeCard(card);
    }

    public int numberOfCard(Card card) {
        if (cards.containsKey(card))
            return cards.get(card).getRepeatedTimes();
        return 0;
    }

    public Deck getSelectedDeck() {
        return decks.size() > selectedDeckIndex ? decks.get(selectedDeckIndex) : null;
    }

    @PostLoad
    private void postLoad() {
        this.cards = new HashMap<>(this.cards);
        this.decks = new ArrayList<>(this.decks);
        this.heroes = new ArrayList<>(this.heroes);
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", creatTime=" + creatTime +
                ", coin=" + coin +
                ", cup=" + cup +
                ", selectedDeckIndex=" + selectedDeckIndex +
                ", cards=" + cards +
                ", heroes=" + heroes +
                ", decks=" + decks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
