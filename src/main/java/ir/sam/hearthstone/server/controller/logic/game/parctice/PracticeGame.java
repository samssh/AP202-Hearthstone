package ir.sam.hearthstone.server.controller.logic.game.parctice;


import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;

import java.util.List;

public class PracticeGame extends AbstractGame {
    public PracticeGame(GameState gameState, ModelLoader modelLoader) {
        super(gameState, modelLoader);
    }

    @Override
    public void selectHero(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        selectHero(side);
    }

    @Override
    public void selectHeroPower(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        selectHeroPower(side);
    }

    @Override
    public void selectMinion(Side client, Side side, int index, int emptyIndex) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        selectMinion(side, index, emptyIndex);
    }

    @Override
    public void selectCardInHand(Side client, Side side, int index) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        selectCardInHand(side, index);
    }

    @Override
    public void endGame(Side client) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        getTimer().cancelTask();
    }

    @Override
    public void nextTurn(Side client) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        nextTurn();
    }

    @Override
    public String getEventLog(Side client) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("number of your deck cards: %d\n"
                , gameState.getDeck(client).size()));
        builder.append(String.format("number of opponent deck cards: %d\n"
                , gameState.getDeck(client.getOther()).size()));
        if (gameState.getActiveQuest(Side.PLAYER_ONE) != null) {
            builder.append(String.format("PLAYER_ONE quest progress: %d\n"
                    , gameState.getActiveQuest(Side.PLAYER_ONE).getQuestProgress()));
        }
        if (gameState.getActiveQuest(Side.PLAYER_TWO) != null) {
            builder.append(String.format("PLAYER_TWO quest progress: %d\n"
                    , gameState.getActiveQuest(Side.PLAYER_TWO).getQuestProgress()));
        }
        builder.append("=====events=====\n");
        List<GameEvent> gameEvents = gameState.getGameEvents();
        for (int i = gameEvents.size() - 1; i >= 0; i--) {
            builder.append(gameState.getGameEvents().get(i).toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    protected PlayDetails.Event observe(Side client, PlayDetails.Event event) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        return event;
    }
}
