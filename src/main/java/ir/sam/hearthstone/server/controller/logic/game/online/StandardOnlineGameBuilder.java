package ir.sam.hearthstone.server.controller.logic.game.online;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.logic.game.AbstractGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilder;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.response.ChangeCardOnPassive;
import ir.sam.hearthstone.server.model.response.PassiveDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;


import java.util.ArrayList;

import static ir.sam.hearthstone.server.controller.Constants.STARTING_HAND_CARDS;

public class StandardOnlineGameBuilder extends AbstractGameBuilder implements OnlineGameBuilder {
    private final Connector connector;


    public StandardOnlineGameBuilder(ModelLoader modelLoader, Connector connector) {
        super(modelLoader);
        this.connector = connector;
    }

    @Override
    protected synchronized void build0() {
        result = new StandardOnlineGame(gameStateBuilder.build(), modelLoader, connector);
        result.startGame();
        sendEvents(Side.PLAYER_ONE);
        sendEvents(Side.PLAYER_TWO);
    }

    @Override
    public synchronized Response setPassive(Side client, Passive passive, ClientHandler clientHandler) {
        if (sideBuilderMap.get(client).getSentPassives().contains(passive)) {
            gameStateBuilder.setPassive(client, passive);
            deckToList(sideBuilderMap.get(client).getDeck(), gameStateBuilder.getDeck(client));
            pickCards(sideBuilderMap.get(client).getHand(), sideBuilderMap.get(client).getHandState()
                    , sideBuilderMap.get(client).getDeck(), STARTING_HAND_CARDS);
            return sendCards(sideBuilderMap.get(client).getHand(), sideBuilderMap.get(client)
                    .getHandState());
        }
        return null;
    }

    @Override
    public synchronized Response setDeck(Side client, Deck deck) {
        gameStateBuilder.setDeck(client, deck);
        return sendPassives(client, "select your passive");
    }

    @Override
    public synchronized Response selectCard(Side client, int index) {
        return new ChangeCardOnPassive(changeState(sideBuilderMap.get(client).getHand()
                , sideBuilderMap.get(client).getHandState(), index), index);
    }

    @Override
    public synchronized Response confirm(Side client) {
        finalizeHand(sideBuilderMap.get(client).getHand(), sideBuilderMap.get(client).getHandState()
                , sideBuilderMap.get(client).getDeck());
        gameStateBuilder.setHand(client, sideBuilderMap.get(client).getHand())
                .setDeckCards(client, sideBuilderMap.get(client).getDeck());
        if (gameStateBuilder.getHand(client.getOther()) != null) {
            build0();
            return result.getResponse(client);
        } else return new PassiveDetails(new ArrayList<>(), null, null,
                "waiting for opponent");
    }

    @Override
    public synchronized void setClientHandler(Side client, ClientHandler clientHandler) {
        gameStateBuilder.setClientHandler(client,clientHandler);
    }

    @Override
    public Response check(Side client) {
        if (result != null) {
            gameStateBuilder.getClientHandler(client).setGame(result);
            return result.getResponse(client);
        }
        return null;
    }
}
