package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Minion;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.events.PlayCard;
import ir.sam.hearthstone.server.logic.game.events.SummonMinion;
import ir.sam.hearthstone.view.model.MinionOverview;
import lombok.Getter;
import lombok.Setter;

public class MinionLogic extends CardLogic {
    @Getter
    @Setter
    private Minion minion;
    @Getter
    @Setter
    private int hp, attack, lastAttackTurn;
    @Getter
    @Setter
    private boolean hasRush, hasTaunt, hasDivineShield, hasAttack;

    public MinionLogic(Side side, Minion minion) {
        super(side);
        this.minion = minion.clone();
        lastAttackTurn = 0;
        hasAttack = false;
        hasDivineShield = false;
        hasTaunt = false;
        hasRush = false;
    }

    @Override
    public String getName() {
        return minion.getName();
    }

    private boolean dealDamage(int damage, AbstractGame game, boolean sendEvent) {
        if (hasDivineShield) {
            hasDivineShield = false;
        } else {
            if (damage > hp) {
                overkill(game, sendEvent);
                return false;
            } else if (damage == hp) {
                kill(game, sendEvent);
                return false;
            } else {
                hp -= damage;
            }
        }
        return true;
    }

    public void dealSpellDamage(int damage, AbstractGame game, boolean sendEvent) {
        boolean ds = hasDivineShield;
        if (dealDamage(damage, game, sendEvent)) {
            if (!ds)
                AbstractGame.visitAll(game, ActionType.SPELL_DAMAGE, this, side.getOther());
            if (sendEvent)
                addChangeEvent(game.getGameState());
        }
    }

    public void giveTaunt(AbstractGame game) {
        if (!hasTaunt) {
            hasTaunt = true;
            game.getGameState().changeTaunts(side, 1);
        }
    }

    public void kill(AbstractGame game, boolean sendEvent) {
        int indexOnGround = game.getGameState().getGround(side).indexOf(this);
        game.getGameState().getGround(side).remove(indexOnGround);
        if (hasTaunt)
            game.getGameState().changeTaunts(side, -1);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.REMOVE_FROM_GROUND)
        .setSide(side.getIndex()).setIndex(indexOnGround).build();
        if (sendEvent)
            game.getGameState().getEvents().add(event);
        game.getActionHolderMap().get(ActionType.DEATH_RATTLE).doAction(getName(), this, game);
        AbstractGame.visitAll(game, ActionType.KILL_MINION, this, side);
    }

    public void overkill(AbstractGame game, boolean sendEvent) {
        kill(game, sendEvent);
        game.getActionHolderMap().get(ActionType.OVERKILL).doAction(this.getName(), this, game);
    }


    /**
     * attack enemy to this minion
     * this can be hero with weapon or minion
     *
     * @param damage damage that deals to this minion
     */
    public void dealMinionDamage(int damage, AbstractGame game, boolean sendEvent) {
        boolean ds = hasDivineShield;
        if (dealDamage(damage, game, sendEvent)) {
            if (!ds)
                game.getActionHolderMap().get(ActionType.MINION_TAKE_DAMAGE)
                        .doAction(getName(), this, game);
            if (sendEvent)
                addChangeEvent(game.getGameState());
        }else hp=0;
    }

    private void addChangeEvent(GameState gameState) {
        int indexOnGround = gameState.getGround(side).indexOf(this);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.CHANGE_IN_GROUND)
                .setOverview(getMinionOverview()).setSide(side.getIndex()).setIndex(indexOnGround).build();
        gameState.getEvents().add(event);
    }


    /**
     * deal damage from passives and hero powers
     *
     * @param damage damage that deals to this minion
     */
    public void dealHeroPowerDamage(int damage, AbstractGame game) {

    }

    public void summon(AbstractGame game, int indexOnGround, int indexOnHand) {
        summon0(game, indexOnGround);
        GameState gameState = game.getGameState();
        gameState.getHand(side).remove(indexOnHand);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.MOVE_FROM_HAND_TO_GROUND)
        .setOverview(getMinionOverview()).setSide(side.getIndex()).setIndex(indexOnGround)
                .setSecondIndex(indexOnHand).build();
        gameState.getEvents().add(event);
        AbstractGame.visitAll(game, ActionType.SUMMON_MINION, this, side);
    }

    /**
     * summon minion without playing this
     */
    public void summon(AbstractGame game, int indexOnGround) {
        summon0(game, indexOnGround);
        GameState gameState = game.getGameState();
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_GROUND)
                .setOverview(getMinionOverview()).setSide(side.getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new SummonMinion(side, minion);
        gameState.getGameEvents().add(gameEvent);
        AbstractGame.visitAll(game, ActionType.SUMMON_MINION, this, side);
    }

    private void summon0(AbstractGame abstractGame, int indexOnGround) {
        hp = minion.getHpFrz();
        attack = minion.getAttFrz();
        GameState gameState = abstractGame.getGameState();
        lastAttackTurn = gameState.getTurnNumber();
        gameState.getGround(side).add(indexOnGround, this);
    }

    public boolean canAttackToMinion(GameState gameState) {
        if (gameState.getTurnNumber() > lastAttackTurn)
            return true;
        return hasRush;
    }

    public boolean canAttackToHero(GameState gameState) {
        if (gameState.getTurnNumber() > lastAttackTurn)
            return gameState.getTaunts(side.getOther())==0;
        return false;
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
        if (sideMana >= minion.getManaFrz()) {
            gameState.setMana(side, sideMana - minion.getManaFrz());
            int indexOnHand = gameState.getHand(side).indexOf(this);
            GameEvent gameEvent = new PlayCard(side, minion);
            gameState.getGameEvents().add(gameEvent);
            game.getActionHolderMap().get(ActionType.BATTLE_CRY).doAction(getName(), this, game);
            AbstractGame.visitAll(game, ActionType.PLAY_MINION, this, side);
            this.summon(game, indexOnGround, indexOnHand);
            AbstractGame.visitAll(game, ActionType.ENEMY_PLAY_MINION, this, side.getOther());
        }
    }

    @Override
    public String toString() {
        return "MinionLogic{" +
                "minion=" + minion +
                '}';
    }
}
