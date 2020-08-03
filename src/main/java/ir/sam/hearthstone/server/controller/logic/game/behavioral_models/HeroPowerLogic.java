package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.HeroPower;
import lombok.Getter;
import lombok.Setter;

public class HeroPowerLogic implements ComplexLogic {
    @Getter
    protected HeroPower heroPower;
    @Getter
    protected final Side side;
    @Getter
    @Setter
    protected int lastTurnUse;

    public HeroPowerLogic(Side side, HeroPower heroPower) {
        this.side = side;
        this.heroPower = heroPower.clone();
        lastTurnUse = 0;
    }

    @Override
    public String getName() {
        return heroPower.getName();
    }
}
