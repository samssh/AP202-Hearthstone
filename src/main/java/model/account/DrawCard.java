package model.account;

import lombok.Getter;
import lombok.Setter;
import model.main.Card;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DrawCard extends GameEvent{
    @ManyToOne
    @Getter
    @Setter
    private Card card;

    public DrawCard(Card card) {
        this.card = card;
    }

    public DrawCard() {}

    @Override
    public String toString() {
        return "DrawCard(" +
                "card=" + card +
                ')';
    }
}