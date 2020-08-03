package ir.sam.hearthstone.server.controller.logic.game.visitors;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.ComplexLogic;

import java.util.Map;

public class ActionHolder {
    private final Map<String, Action> actions;

    public ActionHolder(Map<String, Action> actions) {
        this.actions = actions;
    }

    public void doAction(ComplexLogic visitor, CharacterLogic characterLogic, AbstractGame game) {
        actions.get(visitor.getName()).doAction(visitor, characterLogic, game);
    }
}
