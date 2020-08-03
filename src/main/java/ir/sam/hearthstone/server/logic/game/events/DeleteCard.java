package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;
import lombok.Setter;

public class DeleteCard extends GameEvent {

    @Getter
    @Setter
    private Card card;

    public DeleteCard(Side side, Card card) {
        super(side);
        this.card = card;
    }


    @Override
    public String toString() {
        return side + ": Delete card(" + "card=" + card.getName() + ')';
    }
}
