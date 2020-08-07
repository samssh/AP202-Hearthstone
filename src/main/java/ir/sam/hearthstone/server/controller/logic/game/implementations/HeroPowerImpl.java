package ir.sam.hearthstone.server.controller.logic.game.implementations;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.ComplexLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.HeroLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.MinionLogic;
import ir.sam.hearthstone.server.model.response.PlayDetails;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("ALL")
public class HeroPowerImpl {
    private HeroPowerImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    /**
     * DO_ACTION
     */
    private static void rubbery(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getHeroPower(side).getHeroPower().getManaFrz() > gameState.getMana(side))
            return;
        if (gameState.getHeroPower(side).getLastTurnUse() == gameState.getTurnNumber())
            return;
        gameState.setMana(side, gameState.getMana(side) -
                gameState.getHeroPower(side).getHeroPower().getManaFrz());
        gameState.getHeroPower(side).setLastTurnUse(gameState.getTurnNumber());
        if (gameState.getDeck(side.getOther()).size() > 0) {
            int randomIndex = (int) (gameState.getDeck(side.getOther()).size() * Math.random());
            game.drawCard(side, gameState.getDeck(side.getOther()).remove(randomIndex));
        }
        if (gameState.getActiveWeapon(side) != null
                && gameState.getHand(side.getOther()).size() > 0) {
            int randomIndex = (int) (gameState.getHand(side.getOther()).size() * Math.random());
            game.drawCard(side, gameState.getHand(side.getOther()).remove(randomIndex));
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.REMOVE_FROM_HAND)
                    .setSide(side.getOther().getIndex()).setIndex(randomIndex).build();
            gameState.getEvents().add(event);
        }
    }

    /**
     * DO_ACTION
     */
    private static void fireblast(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic == null) {
            if (gameState.getMana(side) >= gameState
                    .getHeroPower(side).getHeroPower().getManaFrz()
                    && gameState.getTurnNumber() > gameState.getHeroPower(side).getLastTurnUse()) {
                gameState.resetSelected(side);
                gameState.setHeroPowerSelected(side, true);
            }
            return;
        }
        if (characterLogic.getSide() != gameState.getSideTurn()) {
            gameState.setMana(side, gameState.getMana(side) -
                    gameState.getHeroPower(side).getHeroPower().getManaFrz());
            gameState.getHeroPower(side).setLastTurnUse(gameState.getTurnNumber());
            gameState.resetSelected(side);
            if (characterLogic instanceof MinionLogic) {
                PlayDetails.Event event = new PlayDetails.EventBuilder(
                        PlayDetails.EventType.ATTACK_HERO_POWER_TO_MINION)
                        .setSide(gameState.getSideTurn().getIndex())
                        .setIndex(gameState.getGround(characterLogic.getSide()).indexOf(characterLogic))
                        .setSecondIndex(characterLogic.getSide().getIndex()).build();
                gameState.getEvents().add(event);
                ((MinionLogic) characterLogic).dealDamage(1, game, true);
            } else if (characterLogic instanceof HeroLogic) {
                PlayDetails.Event event = new PlayDetails.EventBuilder(
                        PlayDetails.EventType.ATTACK_HERO_POWER_TO_HERO)
                        .setSide(gameState.getSideTurn().getIndex())
                        .setSecondIndex(characterLogic.getSide().getIndex()).build();
                gameState.getEvents().add(event);
                ((HeroLogic) characterLogic).dealDamage(1, game, true);
            }
        }
    }

    /**
     * DO_ACTION
     */
    private static void lifeTap(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        int mode = 0;
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getHeroPower(side).getHeroPower().getManaFrz() > gameState.getMana(side))
            return;
        if (gameState.getHeroPower(side).getLastTurnUse() == gameState.getTurnNumber())
            return;
        gameState.setMana(side, gameState.getMana(side) -
                gameState.getHeroPower(side).getHeroPower().getManaFrz());
        gameState.getHeroPower(side).setLastTurnUse(gameState.getTurnNumber());
        if (gameState.getGround(side).size() > 0) {
            mode = (int) (Math.random() * 2);
        }
        gameState.getHero(side).dealDamage(2, game, true);
        switch (mode) {
            case 0:
                game.drawCard(side);
                break;
            case 1:
                int randomIndex = (int) (Math.random() * gameState.getGround(side).size());
                gameState.getGround(side).get(randomIndex).gain(1, 1, gameState, true, false);
                break;
        }
    }

    /**
     * DO_ACTION
     */
    private static void heal(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (characterLogic == null) {
            if (gameState.getMana(side) >= gameState
                    .getHeroPower(side).getHeroPower().getManaFrz()
                    && gameState.getTurnNumber() > gameState.getHeroPower(side).getLastTurnUse()) {
                gameState.resetSelected(side);
                gameState.setHeroPowerSelected(side, true);
            }
            return;
        }
        if (characterLogic.getSide() == side) {
            gameState.setMana(side, gameState.getMana(side) -
                    gameState.getHeroPower(side).getHeroPower().getManaFrz());
            gameState.getHeroPower(side).setLastTurnUse(gameState.getTurnNumber());
            gameState.resetSelected(side);
            if (characterLogic instanceof MinionLogic) {
                ((MinionLogic) characterLogic).restore(4, gameState);
                PlayDetails.Event event = new PlayDetails.EventBuilder(
                        PlayDetails.EventType.ATTACK_HERO_POWER_TO_MINION)
                        .setSide(side.getIndex())
                        .setIndex(gameState.getGround(side).indexOf(characterLogic))
                        .setSecondIndex(side.getIndex()).build();
                gameState.getEvents().add(event);
            } else if (characterLogic instanceof HeroLogic) {
                ((HeroLogic) characterLogic).restore(4, gameState);
                PlayDetails.Event event = new PlayDetails.EventBuilder(
                        PlayDetails.EventType.ATTACK_HERO_POWER_TO_HERO)
                        .setSide(gameState.getSideTurn().getIndex())
                        .setSecondIndex(side.getIndex()).build();
                gameState.getEvents().add(event);
            }
        }
    }

    /**
     * ENEMY_PLAY_MINION
     */
    private static void caltrops(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic) {
            ((MinionLogic) characterLogic).dealDamage(1, game, true);
        }
    }
}