package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.events.DrawCard;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolder;
import ir.sam.hearthstone.server.logic.game.visitors.ActionHolderBuilder;
import ir.sam.hearthstone.view.model.CardOverview;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractGame {
    @Getter
    protected final GameState gameState;
    @Getter
    protected final Map<ActionType, ActionHolder> actionHolderMap;

    public AbstractGame(GameState gameState, ModelLoader modelLoader) {
        this.gameState = gameState;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
    }

    public abstract void nextTurn();

    public abstract String getEventLog(Side side);

    public abstract List<PlayDetails.Event> getEvents(Side side);

    public void drawCard(Side side){
        int randomIndex = (int) (Math.random()*gameState.getDeck(side).size());
        CardLogic randomCard =  gameState.getDeck(side).get(randomIndex);
        drawCard(side,randomCard);
    }

    public void drawCard(Side side, CardLogic cardLogic){
        gameState.getDeck(side).remove(cardLogic);
        gameState.getHand(side).add(cardLogic);
        PlayDetails.Event event = new PlayDetails.Event(PlayDetails.EventType.ADD_TO_HAND
                ,new CardOverview(cardLogic.getCard(),1,false),side.getIndex());
        GameEvent gameEvent = new DrawCard(side,cardLogic.getCard());
        gameState.getEvents().add(event);
        gameState.getGameEvents().add(gameEvent);
    }

    public Response getResponse(Side side){
        PlayDetails response = new PlayDetails(getEventLog(side),gameState.getMana());
        response.getEvents().addAll(getEvents(side));
        return response;
    }
}

