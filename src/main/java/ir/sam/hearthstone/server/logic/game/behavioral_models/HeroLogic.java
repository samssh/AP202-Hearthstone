package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Hero;
import ir.sam.hearthstone.server.logic.game.Side;

public class HeroLogic extends CharacterLogic {
    private Hero hero;

    public HeroLogic(Side side, Hero hero) {
        super(side);
        this.hero = hero.clone();
    }

    @Override
    public String getName() {
        return hero.getName();
    }
}
