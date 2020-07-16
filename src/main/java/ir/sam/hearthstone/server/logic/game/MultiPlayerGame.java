package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.util.TaskTimer;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerGame extends AbstractGame {
    private final TaskTimer timer;

    public MultiPlayerGame(Server server,GameState gameState, ModelLoader modelLoader) {
        super(server, gameState, modelLoader);
        timer = new TaskTimer();
    }

    @Override
    public void nextTurn() {
        timer.cancelTask();
        if (gameState.getTurnNumber() == 0) {
            init(Side.PLAYER_ONE);
            init(Side.PLAYER_TWO);
        } else {
            visitAll(this,ActionType.END_TURN,null,gameState.getSideTurn());
            drawCard(gameState.getSideTurn().getOther());
        }
        if (gameState.getSideTurn() == Side.PLAYER_TWO) gameState.setTurnNumber(gameState.getTurnNumber() + 1);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        int mana = Math.min(gameState.getTurnNumber(), Server.MAX_MANA);
        gameState.setMana(gameState.getSideTurn(),mana);
        visitAll(this,ActionType.START_TURN,null,gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(server::endTurn,Server.TURN_TIME);
    }

    private void init(Side side) {
        for (CardLogic cardLogic : gameState.getHand(side)) {
            visitAll(this,ActionType.DRAW_CARD,cardLogic,side);
        }
    }

    @Override
    public String getEventLog(Side side) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("number of your deck cards: %d\n", gameState.getDeck(side).size()));
        builder.append(String.format("number of opponent deck cards: %d\n", gameState.getDeck(side.getOther()).size()));
        builder.append("=====events=====\n");
        List<GameEvent> gameEvents = gameState.getGameEvents();
        for (int i = gameEvents.size() - 1; i >= 0; i--) {
            builder.append(gameState.getGameEvents().get(i).toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public List<PlayDetails.Event> getEvents(Side side) {
        List<PlayDetails.Event> result = new ArrayList<>(gameState.getEvents());
        gameState.getEvents().clear();
        return result;
    }
}
