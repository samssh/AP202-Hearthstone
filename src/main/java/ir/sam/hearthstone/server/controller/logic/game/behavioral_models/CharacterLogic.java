package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import lombok.Getter;

public abstract class CharacterLogic implements ComplexLogic {
    @Getter
    protected Side side;

    protected CharacterLogic(Side side) {
        this.side = side;
    }
}
