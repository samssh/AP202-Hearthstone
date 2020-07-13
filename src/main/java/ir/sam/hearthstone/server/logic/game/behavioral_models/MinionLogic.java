package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Minion;

public class MinionLogic extends CardLogic {
    private Minion minion;

    public MinionLogic(Minion minion) {
        this.minion = minion.clone();
    }
}
