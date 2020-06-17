package ir.sam.hearthstone.model.account;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.CardDetails;
import ir.sam.hearthstone.model.main.Hero;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@EqualsAndHashCode(of = "id")
public class Deck implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private long id;
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
    @Setter
    @Getter
    private Hero hero;
    @ElementCollection
    @Setter
    @Getter
    private Map<Card, CardDetails> cards;
    @ManyToOne
    @Getter
    @Setter
    private Player player;

    {
        cards = new HashMap<>();
    }

    public Deck() {
    }

    public Deck(Hero hero, String name) {
        this.hero = hero;
        this.name = name;
    }

    public Deck(Hero hero, String name, Player player) {
        this(hero, name);
        this.player = player;
    }

    public void addCard(Card card) {
        if (cards.containsKey(card)) cards.get(card).vRepeatedTimes(1);
        else cards.put(card, new CardDetails(1));
    }

    public double getWinRate() {
        return games != 0 ? (wins + 0.0) / games : -1;
    }


    public double getManaAverage() {
        double sum = 0, n = 0;
        for (Card card : cards.keySet()) {
            n++;
            sum += card.getManaFrz();
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

    public int getSize() {
        return cards.keySet().stream().mapToInt(card -> cards.get(card).getRepeatedTimes()).sum();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "Id=" + id +
                ", name='" + name + '\'' +
                ", wins=" + wins +
                ", games=" + games +
                ", hero=" + hero +
                ", cards=" + cards +
                '}';
    }

    @PostLoad
    void postLoad() {
        this.cards = new HashMap<>(this.cards);
    }
}
