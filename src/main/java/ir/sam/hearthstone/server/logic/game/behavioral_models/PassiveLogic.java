package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Passive;

public class PassiveLogic implements ComplexLogic{
    private Passive passive;

    public PassiveLogic(Passive passive) {
        this.passive = passive.clone();
    }

    @Override
    public String getName() {
        return passive.getName();
    }
}
