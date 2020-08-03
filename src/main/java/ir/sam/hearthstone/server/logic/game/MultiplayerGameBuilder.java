package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.account.Deck;
import ir.sam.hearthstone.model.main.Passive;
import ir.sam.hearthstone.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.ChangeCardOnPassive;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.view.model.CardOverview;

import java.util.Collections;
import java.util.List;

import static ir.sam.hearthstone.server.Server.STARTING_HAND_CARDS;
import static ir.sam.hearthstone.server.logic.game.Side.*;

public class MultiplayerGameBuilder extends GameBuilder {
    public MultiplayerGameBuilder(ModelLoader modelLoader,Server server) {
        super(modelLoader, server);
    }

    @Override
    protected void build0() {
        result = new MultiPlayerGame(server,gameStateBuilder.build(),modelLoader);
    }

    @Override
    public Response setPassive(Passive passive, Server server) {
        if (sentPassives.contains(passive)) {
            if (gameStateBuilder.getPassiveP1() == null) {
                gameStateBuilder.setPassiveP1(passive);
                return sendPassives("select opponent passive");
            } else {
                gameStateBuilder.setPassiveP2(passive);
                return server.sendDecksForSelection("select opponent deck");
            }
        }
        return null;
    }

    @Override
    public Response setDeckP1(Deck deckP1) {
        gameStateBuilder.setDeckP1(deckP1);
        return sendPassives("select your passive");
    }

    @Override
    public Response setDeckP2(Deck deckP2)  {
        gameStateBuilder.setDeckP2(deckP2);
        deckToList(deckP1,gameStateBuilder.getDeckP1());
        pickCards(handP1,handP1state,deckP1, STARTING_HAND_CARDS);
        return sendCards(handP1,handP1state);
    }

    @Override
    public Response selectCard(int index) {
        if (handP2.size()==0){
            return new ChangeCardOnPassive(changeState(handP1,handP1state,index),index);
        }else {
            return new ChangeCardOnPassive(changeState(handP2,handP2state,index),index);
        }
    }

    @Override
    public Response confirm() {
        if (handP2.size()==0){
            finalizeHand(handP1,handP1state,deckP1);
            gameStateBuilder.setHandP1(handP1).setDeckCardsP1(deckP1);
            deckToList(deckP2,gameStateBuilder.getDeckP2());
            pickCards(handP2,handP2state,deckP2, STARTING_HAND_CARDS);
            return sendCards(handP2,handP2state);
        }else {
            finalizeHand(handP2,handP2state,deckP2);
            gameStateBuilder.setHandP2(handP2).setDeckCardsP2(deckP2);
            build0();
            result.startGame();
            sendEvents(PLAYER_ONE);
            sendEvents(PLAYER_TWO);
            PlayDetails playDetails = new PlayDetails(result.getEventLog(PLAYER_ONE),result.getGameState().getMana()
                    , result.getTurnStartTime());
            playDetails.getEvents().addAll(result.getEvents(PLAYER_ONE));
            return playDetails;
        }
    }

    private void sendEvents(Side side){
        List<CardLogic> hand = result.getGameState().getHand(side);
        Collections.reverse(hand);
        hand.forEach(card -> result.getGameState().getEvents().add(
                new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_HAND)
                .setOverview(new CardOverview(card.getCard())).setSide(side.getIndex()).build()));
        Collections.reverse(hand);
    }
}
