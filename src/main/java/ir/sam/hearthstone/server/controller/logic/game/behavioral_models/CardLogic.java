package ir.sam.hearthstone.server.controller.logic.game.behavioral_models;

import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.main.Card;

public abstract class CardLogic extends CharacterLogic {
    public CardLogic(Side side) {
        super(side);
    }

    public abstract Card getCard();

    public abstract void play(AbstractGame game);
}
