package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.server.logic.game.AbstractGame;

public interface AttackAble {
    void dealMinionDamage(int damage, AbstractGame game);
}
