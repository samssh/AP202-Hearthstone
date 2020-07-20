package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Spell;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.events.PlayCard;

public class SpellLogic extends CardLogic {
    protected Spell spell;

    public SpellLogic(Side side, Spell spell) {
        super(side);
        this.spell = spell.clone();
    }

    @Override
    public Card getCard() {
        return spell;
    }

    @Override
    public void play(AbstractGame game) {
        GameState gameState = game.getGameState();
        int sideMana = gameState.getMana(side);
        if (sideMana >= spell.getManaFrz()) {
            gameState.setMana(side, sideMana - spell.getManaFrz());
            int indexOnHand = gameState.getHand(side).indexOf(this);
            gameState.getHand(side).remove(indexOnHand);
            GameEvent gameEvent = new PlayCard(side, spell);
            gameState.getGameEvents().add(gameEvent);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.PLAY_SPELL)
            .setSide(side.getIndex()).setIndex(indexOnHand).build();
            gameState.getEvents().add(event);
            game.getActionHolderMap().get(ActionType.DO_ACTION).doAction(getName(), this, game);
            AbstractGame.visitAll(game, ActionType.PLAY_SPELL, this, side);
        }
    }

    @Override
    public String getName() {
        return spell.getName();
    }

    @Override
    public String toString() {
        return "SpellLogic{" +
                "spell=" + spell +
                '}';
    }
}
