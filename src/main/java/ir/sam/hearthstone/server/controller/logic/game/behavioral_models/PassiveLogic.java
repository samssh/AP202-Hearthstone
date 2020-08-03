package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Passive;
import lombok.Getter;

public class PassiveLogic implements ComplexLogic {
    @Getter
    protected Passive passive;
    @Getter
    protected final Side side;


    public PassiveLogic(Passive passive, Side side) {
        this.passive = passive.clone();
        this.side = side;
    }

    @Override
    public String getName() {
        return passive.getName();
    }
}
