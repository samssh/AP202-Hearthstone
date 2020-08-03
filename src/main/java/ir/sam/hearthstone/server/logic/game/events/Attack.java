package ir.sam.hearthstone.server.logic.game.events;

import ir.sam.hearthstone.server.model.main.Unit;
import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;

public class Attack extends GameEvent {
    @Getter
    private final Unit attacker, defender;

    public Attack(Side side, Unit attacker, Unit defender) {
        super(side);
        this.attacker = attacker;
        this.defender = defender;
    }

    @Override
    public String toString() {
        return side + ": attack(" + "attacker=" + attacker.getName()
                + "defender=" + defender.getName() + ')';
    }
}
