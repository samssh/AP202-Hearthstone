package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.Constants;
import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.controller.logic.game.events.PlayCard;
import ir.sam.hearthstone.server.controller.logic.game.events.SummonMinion;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.client.MinionOverview;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import lombok.Getter;
import lombok.Setter;

public class MinionLogic extends CardLogic implements LiveCharacter {
    @Getter
    @Setter
    private Minion minion;
    @Getter
    @Setter
    private int hp, attack;
    @Getter
    @Setter
    private boolean hasRush, hasTaunt, hasDivineShield, hasSleep;

    public MinionLogic(Side side, Minion minion) {
        super(side);
        this.minion = minion.clone();
        hasSleep = false;
        hasDivineShield = false;
        hasTaunt = false;
        hasRush = false;
    }

    public void use() {
        this.setHasSleep(true);
        this.setHasRush(false);
    }

    @Override
    public String getName() {
        return minion.getName();
    }

    public void dealDamage(int damage, AbstractGame game, boolean sendEvent) {
        if (hasDivineShield) {
            hasDivineShield = false;
        } else {
            game.getActionHolderMap().get(ActionType.MINION_TAKE_DAMAGE)
                    .doAction(this, this, game);
            if (damage > hp) {
                overkill(game, sendEvent);
                return;
            } else if (damage == hp) {
                kill(game, sendEvent);
                return;
            } else {
                hp -= damage;
            }
        }
        if (sendEvent) addChangeEventOnGround(game.getGameState());
    }

    public void giveTaunt(AbstractGame game) {
        if (!hasTaunt) {
            hasTaunt = true;
            game.getGameState().changeTaunts(side, 1);
        }
    }

    public void giveRush() {
        if (!hasRush) {
            hasRush = true;
        }
    }

    public void removeRushAndGiveSleep(GameState gameState) {
        boolean temp = (this.hasSleep) || (this.hasRush);
        setHasSleep(false);
        setHasRush(false);
        if (temp)
            addChangeEventOnGround(gameState);
    }

    public void kill(AbstractGame game, boolean sendEvent) {
        hp = 0;
        int indexOnGround = game.getGameState().getGround(side).indexOf(this);
        game.getGameState().getGround(side).remove(indexOnGround);
        if (hasTaunt)
            game.getGameState().changeTaunts(side, -1);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.REMOVE_FROM_GROUND)
                .setSide(side.getIndex()).setIndex(indexOnGround).build();
        if (sendEvent)
            game.getGameState().getEvents().add(event);
        game.getActionHolderMap().get(ActionType.DEATH_RATTLE).doAction(this, this, game);
        AbstractGame.visitAll(game, ActionType.KILL_MINION, this, side);
    }

    public void overkill(AbstractGame game, boolean sendEvent) {
        kill(game, sendEvent);
        game.getActionHolderMap().get(ActionType.OVERKILL).doAction(this, this, game);
    }

    private void addChangeEventOnGround(GameState gameState) {
        int indexOnGround = gameState.getGround(side).indexOf(this);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.CHANGE_IN_GROUND)
                .setOverview(getMinionOverview()).setSide(side.getIndex()).setIndex(indexOnGround).build();
        gameState.getEvents().add(event);
    }

    private void addChangeEventOnHand(GameState gameState) {
        int indexOnHand = gameState.getHand(side).indexOf(this);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.CHANGE_IN_HAND)
                .setOverview(new CardOverview(minion)).setSide(side.getIndex()).setIndex(indexOnHand).build();
        gameState.getEvents().add(event);
    }

    public void gain(int attack, int hp, GameState gameState, boolean sendEvent, boolean onHand) {
        if (this.hp <= 0) {
            minion.setAttack(minion.getAttack() + attack);
            minion.setHp(minion.getHp() + hp);
        } else {
            this.attack += attack;
            this.hp += hp;
        }
        if (sendEvent) {
            if (onHand) addChangeEventOnHand(gameState);
            else addChangeEventOnGround(gameState);
        }
    }

    public void summon(AbstractGame game, int indexOnGround, int indexOnHand) {
        summon0(game, indexOnGround);
        GameState gameState = game.getGameState();
        gameState.getHand(side).remove(indexOnHand);
        AbstractGame.visitAll(game, ActionType.SUMMON_MINION, this, side);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.MOVE_FROM_HAND_TO_GROUND)
                .setOverview(getMinionOverview()).setSide(side.getIndex()).setIndex(indexOnGround)
                .setSecondIndex(indexOnHand).build();
        gameState.getEvents().add(event);
    }

    /**
     * summon minion without playing this
     */
    public void summon(AbstractGame game, int indexOnGround) {
        GameState gameState = game.getGameState();
        if (gameState.getGround(side).size() == game.getMaxGroundSize())
            return;
        summon0(game, indexOnGround);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_GROUND)
                .setIndex(indexOnGround).setOverview(getMinionOverview()).setSide(side.getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new SummonMinion(side, minion);
        gameState.getGameEvents().add(gameEvent);
        AbstractGame.visitAll(game, ActionType.SUMMON_MINION, this, side);
    }

    private void summon0(AbstractGame abstractGame, int indexOnGround) {
        hp = minion.getHp();
        attack = minion.getAttack();
        GameState gameState = abstractGame.getGameState();
        hasSleep = true;
        gameState.getGround(side).add(indexOnGround, this);
    }

    public boolean canAttackToMinion() {
        return !hasSleep || hasRush;
    }

    public void restore(int restore, GameState gameState) {
        restore = Math.min(minion.getHp() - hp, restore);
        if (restore > 0) {
            hp += restore;
            addChangeEventOnGround(gameState);
        }
    }

    public boolean canAttackToHero(GameState gameState) {
        return !hasSleep && gameState.getTaunts(side.getOther()) == 0;
    }

    public MinionOverview getMinionOverview() {
        return this.hp > 0 ? new MinionOverview(this) : null;
    }

    @Override
    public Card getCard() {
        return minion;
    }

    @Override
    public void play(AbstractGame game) {
        game.playMinion(this);
    }

    public void play(AbstractGame game, int indexOnGround) {
        GameState gameState = game.getGameState();
        int sideMana = gameState.getMana(side);
        if (sideMana >= minion.getMana()) {
            gameState.setMana(side, sideMana - minion.getMana());
            int indexOnHand = gameState.getHand(side).indexOf(this);
            GameEvent gameEvent = new PlayCard(side, minion);
            gameState.getGameEvents().add(gameEvent);
            game.getActionHolderMap().get(ActionType.BATTLE_CRY).doAction(this, this, game);
            AbstractGame.visitAll(game, ActionType.PLAY_MINION, this, side);
            if (indexOnGround > gameState.getGround(side).size())
                indexOnGround = gameState.getGround(side).size();
            this.summon(game, indexOnGround, indexOnHand);
            AbstractGame.visitAll(game, ActionType.ENEMY_PLAY_MINION, this, side.getOther());
        }
    }
}
