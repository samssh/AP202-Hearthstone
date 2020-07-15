package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.HeroPower;

public class HeroPowerLogic implements ComplexLogic{
    private HeroPower heroPower;

    public HeroPowerLogic(HeroPower heroPower) {
        this.heroPower = heroPower.clone();
    }

    @Override
    public String getName() {
        return heroPower.getName();
    }
}
