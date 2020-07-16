package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.HeroPower;
import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;

public class HeroPowerLogic implements ComplexLogic {
    @Getter
    protected HeroPower heroPower;
    @Getter
    protected final Side side;

    public HeroPowerLogic(Side side, HeroPower heroPower) {
        this.side = side;
        this.heroPower = heroPower.clone();
    }

    @Override
    public String getName() {
        return heroPower.getName();
    }
}
