package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Spell;

public class SpellLogic extends CardLogic {
    private final Spell spell;

    public SpellLogic(Spell spell) {
        this.spell = spell.clone();
    }

    @Override
    public Card getCard() {
        return spell;
    }

    @Override
    public String getName() {
        return spell.getName();
    }
}
