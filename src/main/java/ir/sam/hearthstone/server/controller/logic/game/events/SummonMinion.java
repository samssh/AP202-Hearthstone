package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Minion;

public class SummonMinion extends GameEvent {
    private final Minion minion;

    public SummonMinion(Side side, Minion minion) {
        super(side);
        this.minion = minion;
    }

    @Override
    public String toString() {
        return side + ": SummonMinion{" +
                "minion=" + minion.getName() +
                '}';
    }

    @Override
    public String toString(Side client) {
        return getSideWord(client) + ": SummonMinion{" +
                "minion=" + minion.getName() +
                '}';
    }
}
