package ir.sam.hearthstone.server.logic.game.visitors;

import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.ComplexLogic;

@FunctionalInterface
public interface Action {
    Action dummyAction = (complexLogic, characterLogic, game) -> {
    };

    void doAction(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game);
}