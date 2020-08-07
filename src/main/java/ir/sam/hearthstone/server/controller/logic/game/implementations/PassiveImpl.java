package ir.sam.hearthstone.server.controller.logic.game.implementations;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameState;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CharacterLogic;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.ComplexLogic;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("ALL")
public class PassiveImpl {
    private PassiveImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }


    /**
     * START_TURN
     */
    private static void manaJump(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getMana(side) < 10) {
            gameState.setMana(side, gameState.getMana(side) + 1);
        }
    }

    /**
     * END_TURN
     */
    private static void nurse(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        if (gameState.getGround(gameState.getSideTurn()).size() > 0) {
            int randomIndex = (int) (Math.random() * gameState.getGround(gameState.getSideTurn()).size());
            gameState.getGround(gameState.getSideTurn()).get(randomIndex).restore(2, gameState);
        }
    }

    /**
     * DRAW_CARD
     */
    private static void offCard(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof CardLogic) {
            CardLogic cardLogic = ((CardLogic) characterLogic);
            if (cardLogic.getCard().getMana() > 0)
                cardLogic.getCard().setMana(cardLogic.getCard().getMana() - 1);
        }
    }

    /**
     * START_TURN
     */
    private static void twiceDraw(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.drawCard(game.getGameState().getSideTurn());
    }

    /**
     * KILL_MINION
     */
    private static void warriors(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        int newDefence = game.getGameState().getHero(game.getGameState().getSideTurn()).getDefence() + 2;
        game.getGameState().getHero(game.getGameState().getSideTurn()).setDefence(newDefence, game.getGameState());
    }
}
