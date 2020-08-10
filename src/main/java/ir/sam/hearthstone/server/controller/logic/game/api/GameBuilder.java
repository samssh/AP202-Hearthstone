package ir.sam.hearthstone.server.controller.logic.game.api;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;

public interface GameBuilder {
    Response setDeck(Side client, Deck deck);

    Response setPassive(Side client, Passive passive, ClientHandler clientHandler);

    Response selectCard(Side client, int index);

    Response confirm(Side client) throws DatabaseDisconnectException;

    Game build();
}
