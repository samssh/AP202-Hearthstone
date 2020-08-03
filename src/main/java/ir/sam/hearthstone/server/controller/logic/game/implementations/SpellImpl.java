package ir.sam.hearthstone.server.controller.logic.game.implementations;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.ClassOfCard;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.main.Rarity;
import ir.sam.hearthstone.server.model.response.PlayDetails;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class SpellImpl {
    private SpellImpl() {
        this.arcane(null, null, null);
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    /**
     * DO_ACTION
     */
    private static void arcane(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void book(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void deadlyShot(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn().getOther();
        if (gameState.getGround(side).size() == 0)
            return;
        int indexOnGround = (int) (gameState.getGround(side).size() * Math.random());
        MinionLogic minionLogic = gameState.getGround(side).get(indexOnGround);
        minionLogic.kill(game, true);
    }

    /**
     * DO_ACTION
     */
    private static void divine(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void overflow(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void pharaoh(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void polymorph(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.REMOVE_FROM_GROUND)
                    .setSide(side.getIndex()).setIndex(index).build();
            gameState.getEvents().add(event);
            minionLogic.summon(game, index);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    /**
     * DO_ACTION
     */
    private static void pyroblast(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void sand(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void sandT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic instanceof MinionLogic
                && characterLogic.getSide() == complexLogic.getSide()
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
    private static void sprint(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void starfall(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (complexLogic instanceof SpellLogic) {
            ((SpellLogic) complexLogic).setValue(2);
            AbstractGame.visitAll(game, ActionType.SPELL_DAMAGE, (CharacterLogic) complexLogic, side);
            new Vector<>(gameState.getGround(side.getOther())).forEach(minionLogic -> minionLogic
                    .dealDamage(((SpellLogic) complexLogic).getValue(), game, true));
        }
    }

    /**
     * DO_ACTION
     */
    private static void locusts(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
    private static void boomship(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        for (int i = 0; i < 3; i++) {
            List<MinionLogic> hand = gameState.getHand(side).stream().filter(cardLogic ->
                    cardLogic instanceof MinionLogic).map(cardLogic -> ((MinionLogic) cardLogic))
                    .collect(Collectors.toList());
            if (hand.size() == 0)
                return;
//            System.out.println(hand.stream().map(MinionLogic::getName).collect(Collectors.toList()));
            int randomIndex = (int) (hand.size() * Math.random());
            MinionLogic summon = hand.get(randomIndex);
            summon.giveRush();
            summon.summon(game, gameState.getGround(side).size(), gameState.getHand(side).indexOf(summon));
        }
    }

    /**
     * PLAY_SPELL
     */
    private static void learn(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
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
                gameState.setActiveQuest(side, null);
            }
        }
    }

    /**
     * PLAY_MINION
     */
    private static void strength(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic instanceof MinionLogic && complexLogic instanceof QuestLogic) {
            int progress = ((MinionLogic) characterLogic).getMinion().getManaFrz() * 10;
            ((QuestLogic) complexLogic).setQuestProgress(((QuestLogic) complexLogic).getQuestProgress() + progress);
            if (((QuestLogic) complexLogic).getQuestProgress() >= 100) {
                List<MinionLogic> deck = gameState.getDeck(side).stream().filter(cardLogic ->
                        cardLogic instanceof MinionLogic).map(cardLogic -> ((MinionLogic) cardLogic))
                        .collect(Collectors.toList());
                if (deck.size() == 0) return;
                int randomIndex = (int) (Math.random() * deck.size());
                deck.get(randomIndex).summon(game, gameState.getGround(side).size());
                gameState.setActiveQuest(side, null);
            }
        }
    }
}
