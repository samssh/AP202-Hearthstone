package ir.sam.hearthstone.server.controller.logic.game.online;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.controller.logic.game.events.PlayCard;
import ir.sam.hearthstone.server.model.account.CardDetails;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.account.Player;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;

import java.util.List;


public class StandardOnlineGame extends AbstractGame implements Game {
    protected final Connector connector;


    public StandardOnlineGame(GameState gameState, ModelLoader modelLoader, Connector connector) {
        super(gameState, modelLoader);
        this.connector = connector;
    }

    @Override
    public synchronized void selectHero(Side client, Side side) {
        if (client != gameState.getSideTurn()) return;
        selectHero(side == Side.PLAYER_ONE ? client : client.getOther());
    }

    @Override
    public synchronized void selectHeroPower(Side client, Side side) {
        if (client != gameState.getSideTurn()) return;
        selectHeroPower(side == Side.PLAYER_ONE ? client : client.getOther());
    }

    @Override
    public synchronized void selectMinion(Side client, Side side, int index, int emptyIndex) {
        if (client != gameState.getSideTurn()) return;
        selectMinion(side == Side.PLAYER_ONE ? client : client.getOther(), index, emptyIndex);
    }

    @Override
    public synchronized void selectCardInHand(Side client, Side side, int index) {
        if (client != gameState.getSideTurn()) return;
        selectCardInHand(side == Side.PLAYER_ONE ? client : client.getOther(), index);
    }

    @Override
    public synchronized void endGame(Side client) {
        applyStatistics(client);
        try {
            connector.save(gameState.getClientHandler(client).getPlayer());
            connector.save(gameState.getClientHandler(client.getOther()).getPlayer());
        } catch (DatabaseDisconnectException e) {
            lastException = e;
        }
        running = false;
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                .setSide(client.getIndex()).build();
        gameState.getEvents().add(event);
    }

    protected void applyStatistics(Side loser) {
        applyStatisticsOnPlayer(gameState.getClientHandler(loser).getPlayer(), -1);
        applyStatisticsOnPlayer(gameState.getClientHandler(loser.getOther()).getPlayer(), 1);
        applyStatisticsOnDeck(gameState.getClientHandler(Side.PLAYER_ONE).getPlayer().getSelectedDeck(),
                gameState.getClientHandler(Side.PLAYER_TWO).getPlayer().getSelectedDeck());
    }

    private void applyStatisticsOnPlayer(Player player, int state) {
        player.setCup(Math.max(player.getCup() + state * 10, 0));
        player.getSelectedDeck().setCupEarned(player.getSelectedDeck().getCupEarned() + state * 10);
        player.getSelectedDeck().setGames(player.getSelectedDeck().getGames() + 1);
        player.getSelectedDeck().setWins(player.getSelectedDeck().getWins() + (state + 1) / 2);
    }

    private void applyStatisticsOnDeck(Deck... decks) {
        List<GameEvent> gameEvents = gameState.getGameEvents();
        for (int i = 0; i < gameEvents.size(); i++) {
            GameEvent gameEvent = gameEvents.get(i);
            if (gameEvent instanceof PlayCard) {
                PlayCard playCard = (PlayCard) gameEvent;
                CardDetails details = decks[playCard.getSide().getIndex()].getCards().get(playCard.getCard());
                if (details != null)
                    details.setUsage(details.getUsage() + 1);
            }
        }
    }


    @Override
    public synchronized void nextTurn(Side client) {
        if (client != gameState.getSideTurn()) return;
        nextTurn();
    }

    @Override
    protected String getEventLog(Side client) {
        StringBuilder builder = new StringBuilder();
        if (gameState.getSideTurn() == client)
            builder.append("your turn\n");
        else builder.append("opponent turn\n");
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
        PlayDetails.Event clone = event.clone();
        if (client == Side.PLAYER_TWO) {
            clone.setSide(clone.getSide() ^ 1);
            if (clone.getType() == PlayDetails.EventType.ATTACK_HERO_POWER_TO_HERO
                    || clone.getType() == PlayDetails.EventType.ATTACK_HERO_POWER_TO_MINION)
                clone.setSecondIndex(clone.getSecondIndex() ^ 1);
        }
        if (clone.getSide() == 0) {
            if (clone.getType() == PlayDetails.EventType.END_GAME)
                clone.setMessage("lose");
            return clone;
        }
        switch (event.getType()) {
            case ADD_TO_HAND, MOVE_FROM_GROUND_TO_HAND -> clone.setOverview(CardOverview.BACK);
            case CHANGE_IN_HAND, SHOW_MESSAGE -> {
                return null;
            }
            case END_GAME -> clone.setMessage("win");
        }
        return clone;
    }
}
