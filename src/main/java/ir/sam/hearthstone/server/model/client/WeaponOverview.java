package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.WeaponLogic;
import lombok.Getter;

public class WeaponOverview extends CardOverview {
    @Getter
    private final boolean hasAttack;

    public WeaponOverview(WeaponLogic weaponLogic) {
        super(weaponLogic.getName(), weaponLogic.getName()
                , "class of card: " + weaponLogic.getCard().getClassOfCard().getHeroName()
                , 1, weaponLogic.getCard().getPrice(), weaponLogic.getCard().getManaFrz()
                , weaponLogic.getAttack(), weaponLogic.getUsage(), false);
        hasAttack = weaponLogic.isHasAttack();
    }

    @Override
    public String toString() {
        return "WeaponOverview{" +
                "hasAttack=" + hasAttack +
                ", att=" + att +
                ", hp=" + hp +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
