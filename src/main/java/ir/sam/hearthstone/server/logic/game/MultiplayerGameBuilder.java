package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.account.Deck;
import ir.sam.hearthstone.model.main.Passive;
import ir.sam.hearthstone.response.ChangeCardOnPassive;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;

import java.util.List;

import static ir.sam.hearthstone.server.Server.STARTING_HAND_CARDS;

public class MultiplayerGameBuilder extends GameBuilder {
    public MultiplayerGameBuilder(PlayMode playMode, List<Passive> allPassives) {
        super(playMode, allPassives);
    }

    @Override
    protected void build0() {
        result = new MultiPlayerGame(gameStateBuilder.build());
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
            deckToList(deckP2,gameStateBuilder.getDeckP1());
            pickCards(handP2,handP2state,deckP2, STARTING_HAND_CARDS);
            return sendCards(handP2,handP2state);
        }else {
            finalizeHand(handP2,handP2state,deckP2);
            gameStateBuilder.setHandP2(handP2).setDeckCardsP2(deckP2);
            build();
            //return play panel details
        }
        return null;
    }
}
