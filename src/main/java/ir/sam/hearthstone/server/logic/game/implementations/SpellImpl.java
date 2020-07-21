package ir.sam.hearthstone.server.logic.game.implementations;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.ClassOfCard;
import ir.sam.hearthstone.model.main.Minion;
import ir.sam.hearthstone.model.main.Rarity;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class SpellImpl {
    private SpellImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    /**
     * DO_ACTION
     */
    private void arcane(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(2);
            AbstractGame.visitAll(game, ActionType.SPELL_DAMAGE, (CharacterLogic) complexLogic, side);
            List<LiveCharacter> liveCharacterList = new ArrayList<>(gameState.getGround(side.getOther()));
            liveCharacterList.add(gameState.getHero(side.getOther()));
            int randomIndex = (int) (liveCharacterList.size() * Math.random());
            liveCharacterList.get(randomIndex).dealDamage(((SpellLogic) complexLogic).getValue()
                    , game, true);
        }
    }

    /**
     * DO_ACTION
     */
    private void book(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        for (int i = 0; i < 3; i++) {
            if (gameState.getDeck(side).size() == 0)
                return;
            int indexOnHand = (int) (gameState.getDeck(side).size() * Math.random());
            CardLogic cardLogic = gameState.getDeck(side).remove(indexOnHand);
            if (!(cardLogic instanceof SpellLogic)) {
                game.drawCard(side, cardLogic);
            }
        }
    }

    /**
     * DO_ACTION
     */
    private void deadlyShot(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getGround(side).size() == 0)
            return;
        int indexOnGround = (int) (gameState.getGround(side).size() * Math.random());
        MinionLogic minionLogic = gameState.getGround(side).get(indexOnGround);
        minionLogic.kill(game, true);
    }

    /**
     * DO_ACTION
     */
    private void divine(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(6);
            AbstractGame.visitAll(game, ActionType.SPELL_RESTORE, (CharacterLogic) complexLogic, side);
            List<LiveCharacter> liveCharacters = new LinkedList<>();
            liveCharacters.addAll(gameState.getGround(side));
            liveCharacters.add(gameState.getHero(side));
            liveCharacters.forEach(liveCharacter -> liveCharacter.restore(((SpellLogic) complexLogic)
                    .getValue(), gameState));
        }
    }

    /**
     * DO_ACTION
     */
    private void overflow(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(5);
            AbstractGame.visitAll(game, ActionType.SPELL_RESTORE, (CharacterLogic) complexLogic, side);
            List<LiveCharacter> liveCharacters = new LinkedList<>();
            liveCharacters.addAll(gameState.getGround(side));
            liveCharacters.addAll(gameState.getGround(side.getOther()));
            liveCharacters.add(gameState.getHero(side));
            liveCharacters.add(gameState.getHero(side.getOther()));
            liveCharacters.forEach(liveCharacter -> liveCharacter.restore(((SpellLogic) complexLogic)
                    .getValue(), gameState));
            for (int i = 0; i < 5; i++) {
                if (gameState.getDeck(side).size() == 0)
                    return;
                game.drawCard(side);
            }
        }
    }

    /**
     * DO_ACTION
     */
    private void pharaoh(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void pharaohT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic
                && game.getGameState().getGround(game.getGameState().getSideTurn()).contains(characterLogic)) {
            ((MinionLogic) characterLogic).giveTaunt(game);
            ((MinionLogic) characterLogic).setHasDivineShield(true);
            ((MinionLogic) characterLogic).gain(4, 4, game.getGameState(), true, false);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    /**
     * DO_ACTION
     */
    private void polymorph(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    private final static ClassOfCard neutral = new ClassOfCard("Neutral");

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void polymorphT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn().getOther();
        if (characterLogic instanceof MinionLogic
                && gameState.getGround(side).contains(characterLogic)) {
            int index = gameState.getGround(side).indexOf(characterLogic);
            Minion wisp = new Minion("Wisp", "", 6, neutral
                    , Rarity.Common, 0, 1, 1);
            gameState.getGround(side).remove(index);
            MinionLogic minionLogic = new MinionLogic(side, wisp);
            minionLogic.summon(game, index);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    /**
     * DO_ACTION
     */
    private void pyroblast(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void pyroblastT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn().getOther();
        if (characterLogic instanceof LiveCharacter
                && characterLogic.getSide() != complexLogic.getSide()
                && (gameState.getGround(side).contains(characterLogic) || characterLogic instanceof HeroLogic)
                && complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(10);
            AbstractGame.visitAll(game, ActionType.SPELL_DAMAGE, (CharacterLogic) complexLogic, side);
            ((LiveCharacter) characterLogic).dealDamage(10, game, true);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    /**
     * DO_ACTION
     */
    private void sand(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void sandT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn().getOther();
        if (characterLogic instanceof MinionLogic
                && characterLogic.getSide() != complexLogic.getSide()
                && (gameState.getGround(side).contains(characterLogic))
                && complexLogic instanceof SpellLogic) {
            ((MinionLogic) characterLogic).setHasDivineShield(true);
            ((MinionLogic) characterLogic).gain(1, 2, gameState, true, false);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    /**
     * DO_ACTION
     */
    private void sprint(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        for (int i = 0; i < 4; i++) {
            if (gameState.getDeck(side).size() == 0)
                return;
            game.drawCard(side);
        }
    }

    /**
     * DO_ACTION
     */
    private void starfall(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(2);
            AbstractGame.visitAll(game, ActionType.SPELL_DAMAGE, (CharacterLogic) complexLogic, side);
            gameState.getGround(side.getOther()).forEach(minionLogic -> minionLogic
                    .dealDamage(((SpellLogic) complexLogic).getValue(), game, true));
        }
    }

    /**
     * DO_ACTION
     */
    private void locusts(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        for (int i = 0; i < 7; i++) {
            Minion wisp = new Minion("Wisp", "", 6, neutral
                    , Rarity.Common, 0, 1, 1);
            MinionLogic minionLogic = new MinionLogic(side, wisp);
            minionLogic.summon(game, gameState.getGround(side).size());
        }
    }

    /**
     * DO_ACTION
     */
    private void boomship(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        for (int i = 0; i < 3; i++) {
            List<MinionLogic> hand = gameState.getHand(side).stream().filter(cardLogic ->
                    cardLogic instanceof MinionLogic).map(cardLogic -> ((MinionLogic) cardLogic))
                    .collect(Collectors.toList());
            if (hand.size() == 0)
                return;
            int randomIndex = (int) (hand.size() * Math.random());
            MinionLogic summen = hand.get(randomIndex);
            summen.summon(game, gameState.getGround(side).size(), randomIndex);
        }
    }

    /**
     * PLAY_SPELL
     */
    private void learn(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic instanceof SpellLogic && complexLogic instanceof QuestLogic) {
            int progress = ((SpellLogic) characterLogic).getSpell().getManaFrz() * 100 / 8;
            ((QuestLogic) complexLogic).setQuestProgress(((QuestLogic) complexLogic).getQuestProgress() + progress);
            if (((QuestLogic) complexLogic).getQuestProgress() >= 100) {
                Minion wisp = new Minion("Wisp", "", 6, neutral
                        , Rarity.Common, 0, 6, 6);
                MinionLogic minionLogic = new MinionLogic(side, wisp);
                minionLogic.summon(game, gameState.getGround(side).size());
            }
        }
    }

    /**
     * PLAY_MINION
     */
    private void strength(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic instanceof MinionLogic && complexLogic instanceof QuestLogic) {
            int progress = ((MinionLogic) characterLogic).getMinion().getManaFrz() * 10;
            ((QuestLogic) complexLogic).setQuestProgress(((QuestLogic) complexLogic).getQuestProgress() + progress);
            if (((QuestLogic) complexLogic).getQuestProgress() >= 100) {
                List<MinionLogic> deck = gameState.getDeck(side).stream().filter(cardLogic ->
                        cardLogic instanceof MinionLogic).map(cardLogic -> ((MinionLogic) cardLogic))
                        .collect(Collectors.toList());
                if (deck.size()==0) return;
                int randomIndex = (int) (Math.random()*deck.size());
                deck.get(randomIndex).summon(game,gameState.getGround(side).size());
            }
        }
    }

}
