package ir.sam.hearthstone.server.model.account;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.CardDetails;
import ir.sam.hearthstone.server.model.main.Hero;
import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@ToString()
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player implements SaveAble {
    @Id
    @Setter
    @Getter
    @EqualsAndHashCode.Include
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
    @ElementCollection
    @Setter
    @Getter
    @JoinTable(name = "Player_Card")
    private Map<Card, CardDetails> cards;
    @ManyToMany
    @Setter
    @Getter
    @JoinTable(name = "Player_Hero")
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

    public Player(String userName, String password, long creatTime,
                  int coin, int selectedDeckIndex,
                  Map<Card, CardDetails> cards, List<Hero> heroes, List<Deck> decks) {
        this.userName = userName;
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
}
