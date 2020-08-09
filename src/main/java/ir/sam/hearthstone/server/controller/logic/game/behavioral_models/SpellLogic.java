package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.controller.logic.game.events.PlayCard;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Spell;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import lombok.Getter;
import lombok.Setter;

public class SpellLogic extends CardLogic {
    @Getter
    @Setter
    protected Spell spell;
    @Getter
    @Setter
    protected int value;

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
        if (sideMana >= spell.getMana()) {
            gameState.setMana(side, sideMana - spell.getMana());
            int indexOnHand = gameState.getHand(side).indexOf(this);
            gameState.getHand(side).remove(indexOnHand);
            GameEvent gameEvent = new PlayCard(side, spell);
            gameState.getGameEvents().add(gameEvent);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.PLAY_SPELL)
                    .setOverview(new CardOverview(spell)).setSide(side.getIndex())
                    .setIndex(indexOnHand).build();
            gameState.getEvents().add(event);
            game.getActionHolderMap().get(ActionType.DO_ACTION).doAction(this, this, game);
            AbstractGame.visitAll(game, ActionType.PLAY_SPELL, this, side);
        }
    }

    @Override
    public String getName() {
        return spell.getName();
    }
}
