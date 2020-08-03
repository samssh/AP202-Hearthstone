package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;

public class EndTurn extends GameEvent {

    public EndTurn(Side side) {
        super(side);
    }

    @Override
    public String toString() {
        return side.toString() + ": NextTurn";
    }
}
