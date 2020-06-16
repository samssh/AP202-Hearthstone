package ir.SAM.hearthstone.model.account;

import lombok.Getter;
import lombok.Setter;
import ir.SAM.hearthstone.model.main.Card;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
@Entity
public class DeleteCard extends GameEvent{
    @ManyToOne
    @Getter
    @Setter
    private Card card;

    public DeleteCard(Card card) {
        this.card = card;
    }

    public DeleteCard() {}

    @Override
    public String toString() {
        return "Delete card(" +
                "card=" + card.getName() +
                ')';
    }
}
