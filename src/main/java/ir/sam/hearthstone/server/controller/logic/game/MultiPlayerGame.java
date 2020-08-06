package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.Constants;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.HeroLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.WeaponLogic;
import ir.sam.hearthstone.server.controller.logic.game.events.*;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;

import java.util.ArrayList;
import java.util.List;

import static ir.sam.hearthstone.server.model.response.PlayDetails.EventType.ADD_TO_HAND;
import static ir.sam.hearthstone.server.model.response.PlayDetails.EventType.SHOW_MESSAGE;

public class MultiPlayerGame extends AbstractGame {
    public MultiPlayerGame(GameState gameState, ModelLoader modelLoader) {
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
    public void attackMinionToHero(MinionLogic minionLogic, Side heroSide) {
        int indexOnGround = gameState.getGround(heroSide.getOther()).indexOf(minionLogic);
        HeroLogic heroLogic = gameState.getHero(heroSide);
        heroLogic.dealDamage(minionLogic.getAttack(), this, true);
        minionLogic.use();
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_HERO)
                .setIndex(indexOnGround).setOverview(minionLogic.getMinionOverview())
                .setSide(minionLogic.getSide().getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(minionLogic.getSide(), minionLogic.getCard(), heroLogic.getHero());
        gameState.getGameEvents().add(gameEvent);
    }

    @Override
    public void attackHeroToHero(Side attackerSide) {
        HeroLogic defender = gameState.getHero(attackerSide.getOther());
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        defender.dealDamage(attacker.getAttack(), this, true);
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
    public void attackMinionToMinion(MinionLogic attacker, MinionLogic defender) {
        int indexOfDefender = gameState.getGround(defender.getSide()).indexOf(defender);
        int indexOfAttacker = gameState.getGround(attacker.getSide()).indexOf(attacker);
        attacker.dealDamage(defender.getAttack(), this, false);
        defender.dealDamage(attacker.getAttack(), this, true);
        attacker.use();
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_MINION)
                .setOverview(attacker.getMinionOverview()).setSide(attacker.getSide().getIndex())
                .setIndex(indexOfAttacker)
                .setSecondIndex(indexOfDefender).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attacker.getSide(), attacker.getCard(), defender.getCard());
        gameState.getGameEvents().add(gameEvent);
    }

    @Override
    public void attackHeroToMinion(Side attackerSide, MinionLogic defender) {
        int indexOnGround = gameState.getGround(attackerSide.getOther()).indexOf(defender);
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        HeroLogic heroLogic = gameState.getHero(attackerSide);
        heroLogic.dealDamage(defender.getAttack(), this, false);
        defender.dealDamage(attacker.getAttack(), this, true);
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
    public void startGame() {
        init(Side.PLAYER_ONE);
        init(Side.PLAYER_TWO);
        gameState.setTurnNumber(1);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        int mana = Math.min(gameState.getTurnNumber(), Constants.MAX_MANA);
        gameState.setMana(gameState.getSideTurn(), mana);
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(()->nextTurn(Side.PLAYER_ONE), Constants.TURN_TIME);
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

    @Override
    public void drawCard(Side side) {
        if (gameState.getDeck(side).size() > 0) {
            int randomIndex = (int) (Math.random() * gameState.getDeck(side).size());
            CardLogic randomCard = gameState.getDeck(side).remove(randomIndex);
            drawCard(side, randomCard);
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                    .setMessage(side + " lose").build();
            getGameState().getEvents().add(event);
        }
    }

    @Override
    public void drawCard(Side side, CardLogic cardLogic) {
        if (gameState.getHand(side).size() < Constants.MAX_HAND_SIZE) {
            gameState.getHand(side).add(0, cardLogic);
            visitAll(this, ActionType.DRAW_CARD, cardLogic, side);
            PlayDetails.Event event = new PlayDetails.EventBuilder(ADD_TO_HAND)
                    .setOverview(new CardOverview(cardLogic.getCard())).setSide(side.getIndex()).build();
            GameEvent gameEvent = new DrawCard(side, cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setMessage("your Hand is full!!!!!").build();
            GameEvent gameEvent = new DeleteCard(side, cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        }
    }

    @Override
    public void playMinion(MinionLogic minionLogic) {
        Side side = minionLogic.getSide();
        Minion minion = minionLogic.getMinion();
        if (gameState.getGround(side).size() < Constants.MAX_GROUND_SIZE) {
            if (minion.getManaFrz() <= gameState.getMana(side)) {
                gameState.setSelectedMinionOnHand(side, minionLogic);
            }
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setMessage("ground is full!!!!").build();
            gameState.getEvents().add(event);
        }
    }
}
