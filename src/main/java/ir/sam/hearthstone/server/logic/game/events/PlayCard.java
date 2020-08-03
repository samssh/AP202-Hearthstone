package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;
import lombok.Setter;


public class PlayCard extends GameEvent {
    @Getter
    @Setter
    private Card card;

    public PlayCard(Side side, Card card) {
        super(side);
        this.card = card;
    }

    @Override
    public String toString() {
        return side.toString() + ": PlayCard(" +
                "card=" + card.getName() +
                ')';
    }
}
