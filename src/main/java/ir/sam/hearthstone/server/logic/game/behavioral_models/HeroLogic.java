package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Hero;

public class HeroLogic extends CharacterLogic{
    private Hero hero;

    public HeroLogic(Hero hero) {
        this.hero = hero.clone();
    }
}
