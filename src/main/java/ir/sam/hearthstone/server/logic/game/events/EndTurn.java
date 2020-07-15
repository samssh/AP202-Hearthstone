package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.server.logic.game.Side;

public class EndTurn extends GameEvent {

    public EndTurn(Side side) {
        super(side);
    }

    @Override
    public String toString() {
        return side.toString() + ": NextTurn";
    }
}
