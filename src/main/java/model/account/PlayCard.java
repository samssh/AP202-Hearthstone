package model.account;

import lombok.Getter;
import lombok.Setter;
import model.main.Card;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PlayCard extends GameEvent{
    @ManyToOne
    @Getter
    @Setter
    private Card card;

    public PlayCard(Card card) {
        this.card = card;
    }

    public PlayCard() {}

    @Override
    public String toString() {
        return "PlayCard(" +
                "card=" + card.getName() +
                ')';
    }
}
