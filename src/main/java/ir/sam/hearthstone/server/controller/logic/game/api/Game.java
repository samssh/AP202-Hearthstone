package ir.sam.hearthstone.server.controller.logic.game.api;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.response.Response;

public interface Game {
    void nextTurn(Side client);

    void selectHero(Side client, Side side);

    void selectHeroPower(Side client, Side side);

    void selectMinion(Side client, Side side, int index, int emptyIndex);

    void selectCardInHand(Side client, Side side, int index);

    void endGame(Side client);

    Response getResponse(Side client);
}
