package ir.sam.hearthstone.server.logic.game.visitors;

import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;

@FunctionalInterface
public interface Action {
    Action dummyAction = (characterLogic, abstractGame) -> {
    };

    void doAction(CharacterLogic characterLogic, AbstractGame game);
}