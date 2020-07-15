package ir.sam.hearthstone.server.logic.game.visitors;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;

import java.util.Map;

public class ActionHolder {
    private final Map<String ,Action> actions;
    private final ActionType actionName;

    public ActionHolder(ActionType actionName, Map<String,Action> actions) {
        this.actionName = actionName;
        this.actions = actions;
    }

    public void doAction(String name, CharacterLogic characterLogic, AbstractGame game){
        actions.get(name).doAction(characterLogic,game);
    }
}
