package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.hibernate.SaveAble;
import ir.sam.hearthstone.server.logic.game.Side;

public abstract class GameEvent implements SaveAble {
    protected final Side side;

    public GameEvent(Side side) {
        this.side = side;
    }
}
