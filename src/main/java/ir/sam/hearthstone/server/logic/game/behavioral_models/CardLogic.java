package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.Side;

public abstract class CardLogic extends CharacterLogic {
    public CardLogic(Side side) {
        super(side);
    }

    public abstract Card getCard();

    public abstract void play(AbstractGame game);
}
