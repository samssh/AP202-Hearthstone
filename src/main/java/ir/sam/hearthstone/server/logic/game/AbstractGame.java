package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolder;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolderBuilder;
import ir.sam.hearthstone.util.TaskTimer;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class AbstractGame {
    public static void visitAll(AbstractGame game, ActionType actionType, CharacterLogic characterLogic, Side side) {
        game.getGameState().getSideStream(side).forEach(complexLogic ->
                game.getActionHolderMap().get(actionType)
                        .doAction(complexLogic, characterLogic, game));
    }

    @Getter
    protected final Server server;
    @Getter
    protected final GameState gameState;
    @Getter
    protected final Map<ActionType, ActionHolder> actionHolderMap;
    @Getter
    protected long turnStartTime;
    @Getter
    protected final TaskTimer timer;

    public AbstractGame(Server server, GameState gameState, ModelLoader modelLoader) {
        this.server = server;
        this.gameState = gameState;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
        timer = new TaskTimer();
    }

    public abstract void selectHero(Side client, Side side);

    public abstract void attackMinionToHero(MinionLogic minionLogic, Side heroSide);

    public abstract void attackHeroToHero(Side attackerSide);

    public abstract void selectHeroPower(Side client, Side side);

    public abstract void selectMinion(Side client, Side side, int index, int emptyIndex);

    public abstract void attackMinionToMinion(MinionLogic attacker, MinionLogic defender);

    public abstract void attackHeroToMinion(Side attackerSide, MinionLogic defender);

    public abstract void selectCardInHand(Side client, Side side, int index);

    public abstract void nextTurn(Side client);

    public abstract void startGame();

    public abstract String getEventLog(Side client);

    public abstract List<PlayDetails.Event> getEvents(Side client);

    public abstract void drawCard(Side side);

    public abstract void drawCard(Side side, CardLogic cardLogic);

    public abstract void playMinion(MinionLogic minionLogic);

    public Response getResponse(Side client) {
        PlayDetails response = new PlayDetails(getEventLog(client), gameState.getMana(), turnStartTime);
        response.getEvents().addAll(getEvents(client));
        return response;
    }
}