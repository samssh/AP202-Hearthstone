package ir.sam.hearthstone.server.logic.game.implementations;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Spell;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("ALL")
public class SpecialPowerImpl {
    private SpecialPowerImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    /**
     * DRAW_CARD
     */
    private static void mage(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic != null)
            if (characterLogic instanceof SpellLogic) {
                Spell spell = ((SpellLogic) characterLogic).getSpell();
                spell.setManaFrz(spell.getManaFrz() - 2);
                if (spell.getManaFrz() < 0) spell.setManaFrz(0);
            }
    }

    /**
     * DRAW_CARD
     */
    private static void rogue(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof CardLogic) {
            Card card = ((CardLogic) characterLogic).getCard();
            if (!card.getClassOfCard().getHeroName().equals("Neutral")
                    && !card.getClassOfCard().getHeroName().equals("Rogue")) {
                card.setManaFrz(card.getManaFrz() - 2);
                if (card.getManaFrz() < 0) card.setManaFrz(0);
            }
        }
    }

    /**
     * SPELL_RESTORE
     */
    private static void priest(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof SpellLogic) {
            ((SpellLogic) characterLogic).setValue(2 * ((SpellLogic) characterLogic).getValue());
        }
    }

    /**
     * PLAY_MINION
     */
    private static void hunter(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic) {
            ((MinionLogic) characterLogic).giveRush();
        }
    }
}
