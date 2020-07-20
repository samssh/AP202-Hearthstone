package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Minion;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.logic.game.events.DeleteCard;
import ir.sam.hearthstone.server.logic.game.events.DrawCard;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolder;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolderBuilder;
import ir.sam.hearthstone.view.model.CardOverview;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static ir.sam.hearthstone.response.PlayDetails.EventType.*;

public abstract class AbstractGame {
    public static void visitAll(AbstractGame game, ActionType actionType, CharacterLogic characterLogic, Side side) {
        game.getGameState().getSideStream(side).forEach(complexLogic ->
                game.getActionHolderMap().get(actionType)
                        .doAction(complexLogic.getName(), characterLogic, game));
    }

    protected final Server server;
    @Getter
    protected final GameState gameState;
    @Getter
    protected final Map<ActionType, ActionHolder> actionHolderMap;
    @Getter
    protected long turnStartTime;

    public AbstractGame(Server server, GameState gameState, ModelLoader modelLoader) {
        this.server = server;
        this.gameState = gameState;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
    }

    public abstract void selectHero(Side client,Side side);

    public abstract void selectHeroPower(Side client,Side side);

    public abstract void selectMinion(Side client,Side side, int index, int emptyIndex);

    public abstract void selectCardInHand(Side client,Side side, int index);

    public abstract void nextTurn(Side client);

    public abstract void startGame();

    public abstract String getEventLog(Side client);

    public abstract List<PlayDetails.Event> getEvents(Side client);

    public void drawCard(Side side) {
        int randomIndex = (int) (Math.random() * gameState.getDeck(side).size());
        CardLogic randomCard = gameState.getDeck(side).get(randomIndex);
        drawCard(side, randomCard);
    }

    public void drawCard(Side side, CardLogic cardLogic) {
        gameState.getDeck(side).remove(cardLogic);
        if (gameState.getHand(side).size() < Server.MAX_HAND_SIZE) {
            gameState.getHand(side).add(0, cardLogic);
            PlayDetails.Event event = new PlayDetails.EventBuilder(ADD_TO_HAND)
                    .setOverview(new CardOverview(cardLogic.getCard())).setSide(side.getIndex()).build();
            GameEvent gameEvent = new DrawCard(side, cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        }else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setMessage("your Hand is full!!!!!").build();
            GameEvent gameEvent = new DeleteCard(side,cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        }
    }

    public void playMinion(MinionLogic minionLogic) {
        Side side = minionLogic.getSide();
        Minion minion = minionLogic.getMinion();
        if (gameState.getGround(side).size() < Server.MAX_GROUND_SIZE) {
            if (minion.getManaFrz() <= gameState.getMana(side)) {
                gameState.setSelectedMinionOnHand(side, minionLogic);
            }
        }else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setMessage("ground is full!!!!").build();
            gameState.getEvents().add(event);
        }
    }

    public Response getResponse(Side client) {
        PlayDetails response = new PlayDetails(getEventLog(client), gameState.getMana(), turnStartTime);
        response.getEvents().addAll(getEvents(client));
        return response;
    }
}