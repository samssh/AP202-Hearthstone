package ir.sam.hearthstone.server.logic.game.visitors;

import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.HasAction;
import ir.sam.hearthstone.server.model.main.Hero;
import ir.sam.hearthstone.client.resource_manager.ModelLoader;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.ComplexLogic;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ActionHolderBuilder {
    public static Map<ActionType, ActionHolder> getAllActionHolders(ModelLoader modelLoader) {
        List<HasAction> hasActionList = new LinkedList<>();
        hasActionList.addAll(modelLoader.getHeroes());
        hasActionList.addAll(modelLoader.getCards());
        hasActionList.addAll(modelLoader.getFirstPassives());
        hasActionList.addAll(modelLoader.getHeroes().stream().map(Hero::getPower).collect(Collectors.toList()));
        Map<ActionType, ActionHolder> actionHolderMap = new EnumMap<>(ActionType.class);
        for (ActionType actionType : ActionType.values()) {
            actionHolderMap.put(actionType, new ActionHolderBuilder(actionType).insert(hasActionList).build());
        }
        return actionHolderMap;
    }

    private final Map<String, Action> actions;
    private final ActionType actionName;

    public ActionHolderBuilder(ActionType actionName) {
        this.actionName = actionName;
        actions = new HashMap<>();
    }

    public ActionHolderBuilder insert(List<HasAction> hasActions) {
        hasActions.forEach(this::insert);
        return this;
    }

    public void insert(HasAction hasAction) {
        Action action = Action.dummyAction;
        if (hasAction.getMethods().containsKey(actionName)) {
            try {
                Class<?> clazz = Class.forName(hasAction.getClassName());
                Method method = clazz.getDeclaredMethod(hasAction.getMethods().get(actionName)
                        , ComplexLogic.class, CharacterLogic.class, AbstractGame.class);
                action = toAction(clazz, method, getLookup(clazz));
            } catch (Throwable e) {
                new FindActionException(hasAction.getName(), e).printStackTrace();
            }
        }
        actions.put(hasAction.getName(), action);
    }

    public ActionHolder build() {
        return new ActionHolder(Collections.unmodifiableMap(actions));
    }

    private MethodHandles.Lookup getLookup(Class<?> clazz) {
        MethodHandles.Lookup result;
        try {
            Method method = clazz.getDeclaredMethod("getLookup");
            method.setAccessible(true);
            result = (MethodHandles.Lookup) method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException | ClassCastException e) {
            result = MethodHandles.lookup();
        }
        return result;
    }

    private Action toAction(Class<?> clazz, Method method, MethodHandles.Lookup lookup) throws Throwable {
        MethodType actionType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
        MethodHandle handle = lookup.findStatic(clazz, method.getName(), actionType);
        CallSite site = LambdaMetafactory.metafactory(lookup, "doAction",
                MethodType.methodType(Action.class), actionType, handle, actionType);
        return (Action) site.getTarget().invoke();

    }
}
