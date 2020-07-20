package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Weapon;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.events.PlayCard;
import ir.sam.hearthstone.view.model.WeaponOverview;
import lombok.Getter;
import lombok.Setter;

public class WeaponLogic extends CardLogic {
    @Getter
    @Setter
    protected Weapon weapon;
    @Getter
    @Setter
    private int attack, usage;
    @Getter
    @Setter
    private boolean hasAttack;

    public WeaponLogic(Side side, Weapon weapon) {
        super(side);
        this.weapon = weapon.clone();
    }

    public void setHasAttack(boolean hasAttack, GameState gameState) {
        boolean temp = hasAttack!=this.hasAttack;
        setHasAttack(hasAttack);
        if (temp)
            addChangeEvent(gameState);
    }

    private void addChangeEvent(GameState gameState) {
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.CHANGE_WEAPON)
                .setOverview(getOverview()).setSide(side.getIndex()).build();
        gameState.getEvents().add(event);
    }


    public void use(GameState gameState) {
        hasAttack = false;
        usage--;
        if (usage == 0) {
            gameState.setActiveWeapon(side, null);
        }
    }

    @Override
    public Card getCard() {
        return weapon;
    }

    @Override
    public void play(AbstractGame game) {
        GameState gameState = game.getGameState();
        int mana = gameState.getMana(side);
        if (mana >= weapon.getManaFrz()) {
            gameState.setMana(side, mana - weapon.getManaFrz());
            attack = weapon.getAttFrz();
            usage = weapon.getUsage();
            hasAttack = false;
            int indexOnHand = gameState.getHand(side).indexOf(this);
            gameState.getHand(side).remove(indexOnHand);
            gameState.setActiveWeapon(side, this);
            GameEvent gameEvent = new PlayCard(side, weapon);
            gameState.getGameEvents().add(gameEvent);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.PLAY_WEAPON)
                    .setOverview(getOverview()).setSide(side.getIndex()).setIndex(indexOnHand).build();
            gameState.getEvents().add(event);
            game.getActionHolderMap().get(ActionType.BATTLE_CRY).doAction(getName(), this, game);
            AbstractGame.visitAll(game, ActionType.PLAY_WEAPON, this, side);
        } else {
            System.out.println("cant play weapon because of lack of mana");
        }
    }

    public WeaponOverview getOverview() {
        if (usage == 0) return null;
        return new WeaponOverview(this);
    }

    @Override
    public String getName() {
        return weapon.getName();
    }

    @Override
    public String toString() {
        return "WeaponLogic{" +
                "weapon=" + weapon +
                '}';
    }
}
