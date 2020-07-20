package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.HeroLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.WeaponLogic;
import ir.sam.hearthstone.server.logic.game.events.Attack;
import ir.sam.hearthstone.server.logic.game.events.EndTurn;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.util.TaskTimer;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerGame extends AbstractGame {
    private final TaskTimer timer;

    public MultiPlayerGame(Server server, GameState gameState, ModelLoader modelLoader) {
        super(server, gameState, modelLoader);
        timer = new TaskTimer();
    }

    @Override
    public void selectHero(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (gameState.getWaitForTarget(gameState.getSideTurn()) != null) {
            actionHolderMap.get(ActionType.Do_ACTION_FOR_TARGET)
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn()).getName()
                            , gameState.getHero(side), this);
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
                && gameState.getActiveWeapon(gameState.getSideTurn()).getLastAttackTurn()
                != gameState.getTurnNumber()) {
            gameState.resetSelected(side);
            gameState.setHeroSelected(side, true);
        }
    }

    public void attackMinionToHero(MinionLogic minionLogic, Side heroSide) {
        int indexOnGround = gameState.getGround(heroSide.getOther()).indexOf(minionLogic);
        HeroLogic heroLogic = gameState.getHero(heroSide);
        heroLogic.dealMinionDamage(minionLogic.getAttack(), this, true);
        minionLogic.setLastAttackTurn(gameState.getTurnNumber());
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_HERO)
                .setIndex(indexOnGround).setOverview(minionLogic.getMinionOverview())
                .setSide(minionLogic.getSide().getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(minionLogic.getSide(), minionLogic.getCard(), heroLogic.getHero());
        gameState.getGameEvents().add(gameEvent);
    }

    public void attackHeroToHero(Side attackerSide) {
        HeroLogic defender = gameState.getHero(attackerSide.getOther());
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        defender.dealMinionDamage(attacker.getAttack(), this, true);
        attacker.use(gameState);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_HERO_TO_HERO)
                .setOverview(attacker.getOverview())
                .setSide(attackerSide.getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attackerSide, attacker.getCard(), defender.getHero());
        gameState.getGameEvents().add(gameEvent);
    }


    @Override
    public void selectHeroPower(Side client, Side side) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (side != gameState.getSideTurn()) return;
        gameState.resetSelected(side);
        gameState.setHeroPowerSelected(side, true);
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
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn()).getName()
                            , selected, this);
            return;
        }
        if (side != gameState.getSideTurn()) {
            if (gameState.getTaunts(side) == 0 || selected.isHasTaunt()) {
                if (gameState.getSelectedMinionOnGround(gameState.getSideTurn()) != null) {
                    attackMinionToMinion(gameState.getSelectedMinionOnGround(gameState.getSideTurn()), selected);
                    gameState.resetSelected(side.getOther());
                } else if (gameState.isHeroSelected(side.getOther())) {
                    attackHeroToMinion(side.getOther(),selected);
                    gameState.resetSelected(side.getOther());
                }
            }
        } else {
            if (selected.canAttackToMinion(gameState)) {
                gameState.resetSelected(side);
                gameState.setSelectedMinionOnGround(gameState.getSideTurn(), selected);
            }
        }
    }

    public void attackMinionToMinion(MinionLogic attacker, MinionLogic defender) {
        int indexOfDefender = gameState.getGround(defender.getSide()).indexOf(defender);
        int indexOfAttacker = gameState.getGround(attacker.getSide()).indexOf(attacker);
        attacker.dealMinionDamage(defender.getAttack(), this, false);
        defender.dealMinionDamage(attacker.getAttack(), this, true);
        attacker.setLastAttackTurn(gameState.getTurnNumber());
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_MINION)
                .setOverview(attacker.getMinionOverview()).setSide(attacker.getSide().getIndex())
                .setIndex(indexOfAttacker)
                .setSecondIndex(indexOfDefender).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attacker.getSide(), attacker.getCard(), defender.getCard());
        gameState.getGameEvents().add(gameEvent);
    }

    public void attackHeroToMinion(Side attackerSide, MinionLogic defender) {
        int indexOnGround = gameState.getGround(attackerSide.getOther()).indexOf(defender);
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        HeroLogic heroLogic = gameState.getHero(attackerSide);
        heroLogic.dealMinionDamage(defender.getAttack(), this, false);
        defender.dealMinionDamage(attacker.getAttack(), this, true);
        attacker.use(gameState);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_HERO_TO_MINION)
                .setOverview(attacker.getOverview()).setOverview1(heroLogic.getHeroOverview())
                .setSide(attackerSide.getIndex()).setIndex(indexOnGround).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attackerSide, attacker.getCard(), defender.getCard());
        gameState.getGameEvents().add(gameEvent);
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
    public void nextTurn(Side client) {
        if (client == Side.PLAYER_TWO)
            throw new UnsupportedOperationException();
        timer.cancelTask();
        visitAll(this, ActionType.END_TURN, null, gameState.getSideTurn());
        drawCard(gameState.getSideTurn().getOther());
        GameEvent gameEvent = new EndTurn(gameState.getSideTurn());
        gameState.getGameEvents().add(gameEvent);
        if (gameState.getSideTurn() == Side.PLAYER_TWO) gameState.setTurnNumber(gameState.getTurnNumber() + 1);
        int mana = Math.min(gameState.getTurnNumber(), Server.MAX_MANA);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        gameState.setMana(gameState.getSideTurn(), mana);
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(server::endTurn, Server.TURN_TIME);
    }

    @Override
    public void startGame() {
        init(Side.PLAYER_ONE);
        init(Side.PLAYER_TWO);
        gameState.setTurnNumber(1);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        int mana = Math.min(gameState.getTurnNumber(), Server.MAX_MANA);
        gameState.setMana(gameState.getSideTurn(), mana);
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(server::endTurn, Server.TURN_TIME);
    }

    private void init(Side side) {
        for (CardLogic cardLogic : gameState.getHand(side)) {
            visitAll(this, ActionType.DRAW_CARD, cardLogic, side);
        }
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
