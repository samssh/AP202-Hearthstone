package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.HeroPower;

public class HeroPowerLogic {
    private HeroPower heroPower;

    public HeroPowerLogic(HeroPower heroPower) {
        this.heroPower = heroPower.clone();
    }
}
