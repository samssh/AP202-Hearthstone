package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Weapon;

public class WeaponLogic extends CardLogic {
    private final Weapon weapon;

    public WeaponLogic(Weapon weapon) {
        this.weapon = weapon.clone();
    }

    @Override
    public Card getCard() {
        return weapon;
    }

    @Override
    public String getName() {
        return weapon.getName();
    }
}
