package ir.sam.hearthstone.server.logic.game;

public abstract class AbstractGame {
    protected final GameState gameState;

    public AbstractGame(GameState gameState) {
        this.gameState = gameState;
    }
}

