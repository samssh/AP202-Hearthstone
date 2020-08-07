package ir.sam.hearthstone.server.controller.logic.game.parctice;

import ir.sam.hearthstone.server.controller.Constants;
import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.HeroLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.controller.logic.game.events.EndTurn;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;

import java.util.ArrayList;
import java.util.List;

public class PracticeGame extends AbstractGame {
    public PracticeGame(GameState gameState, ModelLoader modelLoader) {
        super(gameState, modelLoader);
    }

    @Override
    public void selectHero(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (gameState.getWaitForTarget(gameState.getSideTurn()) != null) {
            actionHolderMap.get(ActionType.Do_ACTION_FOR_TARGET)
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn())
                            , gameState.getHero(side), this);
            return;
        }
        HeroLogic selected = gameState.getHero(side);
        if (gameState.isHeroPowerSelected(gameState.getSideTurn())) {
            getActionHolderMap().get(ActionType.DO_ACTION).doAction(
                    gameState.getHeroPower(gameState.getSideTurn()), selected, this);
            return;
        }
        if (side != gameState.getSideTurn()) {
            if (gameState.getSelectedMinionOnGround(side.getOther()) != null) {
                MinionLogic attacker = gameState.getSelectedMinionOnGround(side.getOther());
                if (gameState.getSelectedMinionOnGround(side.getOther()).canAttackToHero(gameState)) {
                    attackMinionToHero(attacker, side);
                    gameState.resetSelected(side.getOther());
                }
            } else if (gameState.isHeroSelected(side.getOther())) {
                attackHeroToHero(side.getOther());
                gameState.resetSelected(side.getOther());
            }
        } else if (gameState.getActiveWeapon(gameState.getSideTurn()) != null
                && gameState.getActiveWeapon(gameState.getSideTurn()).isHasAttack()) {
            gameState.resetSelected(side);
            gameState.setHeroSelected(side, true);
        }
    }

    @Override
    public void selectHeroPower(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (side != gameState.getSideTurn()) return;
        getActionHolderMap().get(ActionType.DO_ACTION).doAction(gameState.getHeroPower(side)
                , null, this);
    }

    @Override
    public void selectMinion(Side client, Side side, int index, int emptyIndex) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (side == gameState.getSideTurn() && gameState.getSelectedMinionOnHand(side) != null) {
            if (emptyIndex > gameState.getGround(side).size())
                emptyIndex = gameState.getGround(side).size();
            gameState.getSelectedMinionOnHand(side).play(this, emptyIndex);
            gameState.setSelectedMinionOnHand(side, null);
            return;
        }
        if (index >= gameState.getGround(side).size()) {
            return;
        }
        MinionLogic selected = gameState.getGround(side).get(index);
        if (gameState.getWaitForTarget(gameState.getSideTurn()) != null) {
            actionHolderMap.get(ActionType.Do_ACTION_FOR_TARGET)
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn())
                            , selected, this);
            return;
        }
        if (gameState.isHeroPowerSelected(gameState.getSideTurn())) {
            getActionHolderMap().get(ActionType.DO_ACTION).doAction(
                    gameState.getHeroPower(gameState.getSideTurn()), selected, this);
            return;
        }
        if (side != gameState.getSideTurn()) {
            if (gameState.getTaunts(side) == 0 || selected.isHasTaunt()) {
                if (gameState.getSelectedMinionOnGround(gameState.getSideTurn()) != null) {
                    attackMinionToMinion(gameState.getSelectedMinionOnGround(gameState.getSideTurn()), selected);
                    gameState.resetSelected(side.getOther());
                } else if (gameState.isHeroSelected(side.getOther())) {
                    attackHeroToMinion(side.getOther(), selected);
                    gameState.resetSelected(side.getOther());
                }
            }
        } else {
            if (selected.canAttackToMinion()) {
                gameState.resetSelected(side);
                gameState.setSelectedMinionOnGround(gameState.getSideTurn(), selected);
            }
        }
    }

    @Override
    public void selectCardInHand(Side client, Side side, int index) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (side != gameState.getSideTurn())
            return;
        gameState.resetSelected(side);
        List<CardLogic> hand = gameState.getHand(side);
        if (index >= hand.size()) {
            return;
        }
        hand.get(index).play(this);
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
        timer.cancelTask();
        visitAll(this, ActionType.END_TURN, null, gameState.getSideTurn());
        drawCard(gameState.getSideTurn().getOther());
        GameEvent gameEvent = new EndTurn(gameState.getSideTurn());
        gameState.getGameEvents().add(gameEvent);
        if (gameState.getSideTurn() == Side.PLAYER_TWO) gameState.setTurnNumber(gameState.getTurnNumber() + 1);
        int mana = Math.min(gameState.getTurnNumber(), Constants.MAX_MANA);
        if (gameState.getActiveWeapon(gameState.getSideTurn()) != null)
            gameState.getActiveWeapon(gameState.getSideTurn()).setHasAttack(false, gameState);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        gameState.setMana(gameState.getSideTurn(), mana);
        if (gameState.getActiveWeapon(gameState.getSideTurn()) != null)
            gameState.getActiveWeapon(gameState.getSideTurn()).setHasAttack(true, gameState);
        gameState.getGround(gameState.getSideTurn()).forEach(
                minionLogic -> minionLogic.removeRushAndGiveSleep(gameState));
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(()->nextTurn(Side.PLAYER_ONE), Constants.TURN_TIME);
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
    public List<PlayDetails.Event> getEvents(Side client) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        List<PlayDetails.Event> result = new ArrayList<>(gameState.getEvents());
        gameState.getEvents().clear();
        return result;
    }
}
