package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Card;
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

    @Override
    public String toString(Side client) {
        return getSideWord(client) + ": PlayCard(" +
                "card=" + card.getName() +
                ')';
    }
}
