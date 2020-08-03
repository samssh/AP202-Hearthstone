package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Card;
import lombok.Getter;
import lombok.Setter;

public class DrawCard extends GameEvent {
    @Getter
    @Setter
    private Card card;

    public DrawCard(Side side, Card card) {
        super(side);
        this.card = card;
    }

    @Override
    public String toString() {
        return side.toString() + ": DrawCard(" +
                "card=" + card.getName() +
                ')';
    }
}
