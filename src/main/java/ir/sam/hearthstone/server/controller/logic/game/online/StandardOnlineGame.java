package ir.sam.hearthstone.server.controller.logic.game.online;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

import java.util.List;


public class StandardOnlineGame extends AbstractGame implements Game {
    protected final Connector connector;

    public StandardOnlineGame(GameState gameState, ModelLoader modelLoader, Connector connector) {
        super(gameState, modelLoader);
        this.connector = connector;
    }

    @Override
    public void selectHero(Side client, Side side) {
        if (client != gameState.getSideTurn()) return;
        selectHero(side == Side.PLAYER_ONE ? client : client.getOther());
    }

    @Override
    public void selectHeroPower(Side client, Side side) {
        if (client != gameState.getSideTurn()) return;
        selectHeroPower(side == Side.PLAYER_ONE ? client : client.getOther());
    }

    @Override
    public void selectMinion(Side client, Side side, int index, int emptyIndex) {
        if (client != gameState.getSideTurn()) return;
        selectMinion(side == Side.PLAYER_ONE ? client : client.getOther(), index, emptyIndex);
    }

    @Override
    public void selectCardInHand(Side client, Side side, int index) {
        if (client != gameState.getSideTurn()) return;
        selectCardInHand(side == Side.PLAYER_ONE ? client : client.getOther(), index);
    }

    @Override
    public void endGame(Side client) {
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                .setSide(client.getIndex()).build();
        gameState.getEvents().add(event);
    }

    @Override
    public void nextTurn(Side client) {
        if (client != gameState.getSideTurn()) return;
        nextTurn();
    }

    @Override
    public String getEventLog(Side client) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("number of your deck cards: %d\n"
                , gameState.getDeck(client).size()));
        builder.append(String.format("number of opponent deck cards: %d\n"
                , gameState.getDeck(client.getOther()).size()));
        if (gameState.getActiveQuest(Side.PLAYER_ONE) != null) {
            builder.append(String.format("quest progress: %d\n"
                    , gameState.getActiveQuest(Side.PLAYER_ONE).getQuestProgress()));
        }
        builder.append("=====events=====\n");
        List<GameEvent> gameEvents = gameState.getGameEvents();
        for (int i = gameEvents.size() - 1; i >= 0; i--) {
            builder.append(gameState.getGameEvents().get(i).toString(client));
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    protected PlayDetails.Event observe(Side client, PlayDetails.Event event) {
        if (client == Side.PLAYER_ONE) return event;
        PlayDetails.Event clone = event.clone();
        clone.setSide(clone.getSide() ^ 1);
        switch (event.getType()) {
            case ADD_TO_HAND, MOVE_FROM_GROUND_TO_HAND -> clone.setOverview(CardOverview.BACK);
            case CHANGE_IN_HAND -> {
                return null;
            }
            case SHOW_MESSAGE -> {
                if (clone.getSide() == 1) return null;
            }
            case END_GAME -> {
                if (clone.getSide() == 0) clone.setMessage("lose");
                else if (clone.getSide() == 1) clone.setMessage("win");
            }
        }
        return clone;
    }
}
