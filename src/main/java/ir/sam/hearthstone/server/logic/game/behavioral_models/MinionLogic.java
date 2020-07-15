package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Minion;

public class MinionLogic extends CardLogic {
    private Minion minion;

    public MinionLogic(Minion minion) {
        this.minion = minion.clone();
    }

    @Override
    public String getName() {
        return minion.getName();
    }

    @Override
    public Card getCard() {
        return minion;
    }
}
