package ir.sam.hearthstone.server.logic.game.implementations;

import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CharacterLogic;

public class ManaJumpImpl {
    public static void increaseMana(CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        if (gameState.getMana(side)<10){
            gameState.setMana(side,gameState.getMana(side)+1);
        }
    }
}
