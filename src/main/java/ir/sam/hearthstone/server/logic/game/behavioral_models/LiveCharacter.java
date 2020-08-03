package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;

public interface LiveCharacter {
    void dealDamage(int damage, AbstractGame game, boolean sendEvent);

    void restore(int restore, GameState gameState);
}
