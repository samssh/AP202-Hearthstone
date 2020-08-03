package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;

public interface LiveCharacter {
    void dealDamage(int damage, AbstractGame game, boolean sendEvent);

    void restore(int restore, GameState gameState);
}
