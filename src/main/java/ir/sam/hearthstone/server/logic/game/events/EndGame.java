package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;
import lombok.Setter;

public class EndGame extends GameEvent {

    @Getter
    @Setter
    private EndGameType type;

    public EndGame(Side side, EndGameType type) {
        super(side);
        this.type = type;
    }

    public enum EndGameType {
        WIN, LOSE
    }

    @Override
    public String toString() {
        return side.toString() + ": EndGame(" +
                "type=" + type +
                ')';
    }
}
