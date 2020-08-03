package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.logic.game.Side;

public class SummonMinion extends GameEvent {
    private final Minion minion;

    public SummonMinion(Side side, Minion minion) {
        super(side);
        this.minion = minion;
    }

    @Override
    public String toString() {
        return side + ": SummonMinion{" +
                "minion=" + minion +
                '}';
    }
}
