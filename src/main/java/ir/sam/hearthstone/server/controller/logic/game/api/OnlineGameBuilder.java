package ir.sam.hearthstone.server.controller.logic.game.api;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;

public interface OnlineGameBuilder extends GameBuilder {
    void setClientHandler(Side client, ClientHandler clientHandler);

    Response check(Side client) throws DatabaseDisconnectException;

    void cancel();
}
