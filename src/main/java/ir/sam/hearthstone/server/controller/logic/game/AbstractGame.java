package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.controller.logic.game.visitors.ActionHolder;
import ir.sam.hearthstone.server.controller.logic.game.visitors.ActionHolderBuilder;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.TaskTimer;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class AbstractGame implements Game {
    public static void visitAll(AbstractGame game, ActionType actionType, CharacterLogic characterLogic, Side side) {
        game.getGameState().getSideStream(side).forEach(complexLogic ->
                game.getActionHolderMap().get(actionType)
                        .doAction(complexLogic, characterLogic, game));
    }

    @Getter
    protected final GameState gameState;
    @Getter
    protected final Map<ActionType, ActionHolder> actionHolderMap;
    @Getter
    protected long turnStartTime;
    @Getter
    protected final TaskTimer timer;

    public AbstractGame(GameState gameState, ModelLoader modelLoader) {
        this.gameState = gameState;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
        timer = new TaskTimer();
    }

    @Override
    public abstract void selectHero(Side client, Side side);

    @Override
    public abstract void selectHeroPower(Side client, Side side);

    @Override
    public abstract void selectMinion(Side client, Side side, int index, int emptyIndex);

    @Override
    public abstract void selectCardInHand(Side client, Side side, int index);

    @Override
    public abstract void nextTurn(Side client);

    public abstract void attackMinionToHero(MinionLogic minionLogic, Side heroSide);

    public abstract void attackHeroToHero(Side attackerSide);

    public abstract void attackMinionToMinion(MinionLogic attacker, MinionLogic defender);

    public abstract void attackHeroToMinion(Side attackerSide, MinionLogic defender);

    public abstract void startGame();

    public abstract String getEventLog(Side client);

    public abstract List<PlayDetails.Event> getEvents(Side client);

    public abstract void drawCard(Side side);

    public abstract void drawCard(Side side, CardLogic cardLogic);

    public abstract void playMinion(MinionLogic minionLogic);

    @Override
    public Response getResponse(Side client) {
        PlayDetails response = new PlayDetails(getEventLog(client), gameState.getMana(), turnStartTime);
        response.getEvents().addAll(getEvents(client));
        return response;
    }
}