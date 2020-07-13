package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Spell;

public class SpellLogic extends CardLogic {
    private final Spell spell;

    public SpellLogic(Spell spell) {
        this.spell = spell.clone();
    }
}
