package ir.sam.hearthstone.server.logic.game.implementations;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;

import java.lang.invoke.MethodHandles;

public class PassiveImpl {
    private PassiveImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    private static void manaJump(CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getMana(side) < 10) {
            gameState.setMana(side, gameState.getMana(side) + 1);
        }
    }

    private static void nurse(CharacterLogic characterLogic, AbstractGame game) {
        System.out.println("nurse");
        GameState gameState = game.getGameState();
        if (gameState.getGround(gameState.getSideTurn()).size() > 0) {
            int randomIndex = (int) (Math.random() * gameState.getGround(gameState.getSideTurn()).size());
            gameState.getGround(gameState.getSideTurn()).get(randomIndex).restore(2, gameState);
        }
    }

    private static void offCard(CharacterLogic characterLogic, AbstractGame game) {
        System.out.println(characterLogic instanceof CardLogic);
        if (characterLogic instanceof CardLogic) {
            CardLogic cardLogic = ((CardLogic) characterLogic);
            if (cardLogic.getCard().getManaFrz() > 0)
                cardLogic.getCard().setManaFrz(cardLogic.getCard().getManaFrz() - 1);
        }
    }

    private static void twiceDraw(CharacterLogic characterLogic, AbstractGame game) {
        game.drawCard(game.getGameState().getSideTurn());
    }

    private static void warriors(CharacterLogic characterLogic, AbstractGame game) {
        System.out.println("warriors");
        int newDefence = game.getGameState().getHero(game.getGameState().getSideTurn()).getDefence() + 2;
        game.getGameState().getHero(game.getGameState().getSideTurn()).setDefence(newDefence,game.getGameState());
    }
}
