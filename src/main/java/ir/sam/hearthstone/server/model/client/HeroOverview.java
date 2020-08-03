package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.HeroLogic;
import lombok.ToString;

@ToString(includeFieldNames = false)
public class HeroOverview extends UnitOverview {
    private final int hp;
    private final int defence;

    public HeroOverview(HeroLogic heroLogic) {
        super(heroLogic.getName(), heroLogic.getName(), null);
        hp = heroLogic.getHp();
        defence = heroLogic.getDefence();
    }
}
