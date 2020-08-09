package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.util.hibernate.SaveAble;

public abstract class GameEvent implements SaveAble {
    protected final Side side;

    public GameEvent(Side side) {
        this.side = side;
    }

    protected String getSideWord(Side client){
        return client==side? "you":"opponent";
    }

    public abstract String toString(Side client);
}
