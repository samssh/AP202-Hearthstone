package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Weapon;
import ir.sam.hearthstone.server.logic.game.Side;

public class WeaponLogic extends CardLogic {
    protected Weapon weapon;

    public WeaponLogic(Side side, Weapon weapon) {
        super(side);
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

    @Override
    public String toString() {
        return "WeaponLogic{" +
                "weapon=" + weapon +
                '}';
    }
}
