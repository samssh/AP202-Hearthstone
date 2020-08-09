package ir.sam.hearthstone.server.controller.logic.game.events;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Unit;
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

    @Override
    public String toString(Side client) {
        return getSideWord(client) + ": attack(" + "attacker=" + attacker.getName()
                + "defender=" + defender.getName() + ')';
    }
}
