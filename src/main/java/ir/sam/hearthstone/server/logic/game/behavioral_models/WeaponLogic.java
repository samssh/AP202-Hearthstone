package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Weapon;

public class WeaponLogic extends CardLogic{
    private final Weapon weapon;
    public WeaponLogic(Weapon weapon){
        this.weapon = weapon.clone();
    }
}
