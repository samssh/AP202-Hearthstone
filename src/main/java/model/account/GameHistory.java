package model.account;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import model.main.Card;
import model.main.Hero;
import model.main.Passive;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameHistory implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @Getter
    @Setter
    private Passive passive;
    @ManyToOne
    @Getter
    @Setter
    private Hero hero;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "GameHistory_deck",
            inverseJoinColumns = {@JoinColumn(name = "Card_Name")})
    @Getter
    @Setter
    private List<Card> deck;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
    @Getter
    @Setter
    private List<GameEvent> events;

    {
        events = new ArrayList<>();
    }

    public GameHistory() {
    }

    public GameHistory(Passive passive, Hero hero, List<Card> deck) {
        this.passive = passive;
        this.hero = hero;
        this.deck = deck;
    }
}
