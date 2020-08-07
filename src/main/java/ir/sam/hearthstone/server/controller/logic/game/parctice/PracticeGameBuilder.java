package ir.sam.hearthstone.server.controller.logic.game.parctice;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.logic.game.AbstractGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.response.ChangeCardOnPassive;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;

import java.util.Collections;
import java.util.List;

import static ir.sam.hearthstone.server.controller.Constants.STARTING_HAND_CARDS;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_TWO;

public class PracticeGameBuilder extends AbstractGameBuilder {
    public PracticeGameBuilder(ModelLoader modelLoader) {
        super(modelLoader);
    }

    @Override
    protected void build0() {
        result = new PracticeGame(gameStateBuilder.build(), modelLoader);
    }

    @Override
    public Response setPassive(Side client, Passive passive, ClientHandler clientHandler) {
        if (client == PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (sentPassives.contains(passive)) {
            if (gameStateBuilder.getPassive(client) == null) {
                gameStateBuilder.setPassive(client, passive);
                return sendPassives("select opponent passive");
            } else {
                gameStateBuilder.setPassive(client.getOther(), passive);
                return clientHandler.sendDecksForSelection("select opponent deck");
            }
        }
        return null;
    }

    @Override
    public Response setDeck(Side client, Deck deck) {
        if (client == PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (gameStateBuilder.getDeck(PLAYER_ONE) == null) {
            gameStateBuilder.setDeck(PLAYER_ONE, deck);
            return sendPassives("select your passive");
        } else {
            gameStateBuilder.setDeck(PLAYER_TWO, deck);
            deckToList(sideBuilderMap.get(PLAYER_ONE).getDeck(), gameStateBuilder.getDeck(PLAYER_ONE));
            pickCards(sideBuilderMap.get(PLAYER_ONE).getHand(), sideBuilderMap.get(PLAYER_ONE).getHandState()
                    , sideBuilderMap.get(PLAYER_ONE).getDeck(), STARTING_HAND_CARDS);
            return sendCards(sideBuilderMap.get(PLAYER_ONE).getHand(), sideBuilderMap.get(PLAYER_ONE).getHandState());
        }

    }

    @Override
    public Response selectCard(Side client, int index) {
        if (client == PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (sideBuilderMap.get(PLAYER_TWO).getHand().size() == 0) {
            return new ChangeCardOnPassive(changeState(sideBuilderMap.get(PLAYER_ONE).getHand()
                    , sideBuilderMap.get(PLAYER_ONE).getHandState(), index), index);
        } else {
            return new ChangeCardOnPassive(changeState(sideBuilderMap.get(PLAYER_TWO).getHand()
                    , sideBuilderMap.get(PLAYER_TWO).getHandState(), index), index);
        }
    }

    @Override
    public Response confirm(Side client) {
        if (client == PLAYER_TWO)
            throw new UnsupportedOperationException();
        if (sideBuilderMap.get(PLAYER_TWO).getHand().size() == 0) {
            finalizeHand(sideBuilderMap.get(PLAYER_ONE).getHand(), sideBuilderMap.get(PLAYER_ONE).getHandState()
                    , sideBuilderMap.get(PLAYER_ONE).getDeck());
            gameStateBuilder.setHand(PLAYER_ONE, sideBuilderMap.get(PLAYER_ONE).getHand())
                    .setDeckCards(PLAYER_ONE, sideBuilderMap.get(PLAYER_ONE).getDeck());
            deckToList(sideBuilderMap.get(PLAYER_TWO).getDeck(), gameStateBuilder.getDeck(PLAYER_TWO));
            pickCards(sideBuilderMap.get(PLAYER_TWO).getHand(), sideBuilderMap.get(PLAYER_TWO).getHandState()
                    , sideBuilderMap.get(PLAYER_TWO).getDeck(), STARTING_HAND_CARDS);
            return sendCards(sideBuilderMap.get(PLAYER_TWO).getHand(), sideBuilderMap.get(PLAYER_TWO).getHandState());
        } else {
            finalizeHand(sideBuilderMap.get(PLAYER_TWO).getHand(), sideBuilderMap.get(PLAYER_TWO).getHandState()
                    , sideBuilderMap.get(PLAYER_TWO).getDeck());
            gameStateBuilder.setHand(PLAYER_TWO,sideBuilderMap.get(PLAYER_TWO).getHand()).setDeckCards(PLAYER_TWO
                    ,sideBuilderMap.get(PLAYER_TWO).getDeck());
            build0();
            result.startGame();
            sendEvents(PLAYER_ONE);
            sendEvents(PLAYER_TWO);
            PlayDetails playDetails = new PlayDetails(result.getEventLog(PLAYER_ONE), result.getGameState().getMana()
                    , result.getTurnStartTime());
            playDetails.getEvents().addAll(result.getEvents(PLAYER_ONE));
            return playDetails;
        }
    }

    private void sendEvents(Side side) {
        List<CardLogic> hand = result.getGameState().getHand(side);
        Collections.reverse(hand);
        hand.forEach(card -> result.getGameState().getEvents().add(
                new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_HAND)
                        .setOverview(new CardOverview(card.getCard())).setSide(side.getIndex()).build()));
        Collections.reverse(hand);
    }
}
