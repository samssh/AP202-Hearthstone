package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.HeroLogic;
import lombok.Getter;
import lombok.ToString;

public class HeroOverview extends UnitOverview {
    @Getter
    private final int hp;
    @Getter
    private final int defence;

    public HeroOverview(HeroLogic heroLogic) {
        super(heroLogic.getName(), heroLogic.getName(), null);
        hp = heroLogic.getHp();
        defence = heroLogic.getDefence();
    }

    @Override
    public String toString() {
        return "HeroOverview{" +
                "hp=" + hp +
                ", defence=" + defence +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
