package model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Collection;

@Entity (name = "Card")
abstract public class Card extends Unit {
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @Setter
    @Getter
    ClassOfCard classOfCard;
    @Column
    @Setter
    @Getter
    Rarity rarity;
    @Column
    @Setter
    @Getter
    int manaFrz;
    @Column
    @Setter
    @Getter
    int price;

    // only hibernate use this constructor
    public Card() {
    }

    Card(String name, String description, int price,
         ClassOfCard classOfCard,
         Rarity rarity, int manaFrz) {
        super(name, description);
        this.classOfCard = classOfCard;
        this.rarity = rarity;
        this.manaFrz = manaFrz;
        this.price = price;
    }

    @ManyToMany(mappedBy = "cards")
    private Collection<Player> Players;

    public Collection<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(Collection<Player> players) {
        Players = players;
    }
}
