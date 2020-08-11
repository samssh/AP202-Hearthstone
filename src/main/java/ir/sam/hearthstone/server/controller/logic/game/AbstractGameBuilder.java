package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.ClientHandler;
import ir.sam.hearthstone.server.controller.logic.game.api.GameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.CardLogic;
import ir.sam.hearthstone.server.model.account.CardDetails;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.client.PassiveOverview;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.response.PassiveDetails;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;

import java.util.*;

import static ir.sam.hearthstone.server.controller.Constants.STARTING_PASSIVES;


public abstract class AbstractGameBuilder implements GameBuilder {
    protected AbstractGame result;
    protected final GameStateBuilder gameStateBuilder;
    protected final List<Passive> allPassives;
    protected final Map<Side, SideBuilder> sideBuilderMap;
    protected final ModelLoader modelLoader;
    protected boolean canceled;

    protected static class SideBuilder {
        @Getter
        protected final List<Passive> sentPassives;
        @Getter
        private final List<Card> hand, deck;
        @Getter
        private final List<Boolean> handState;

        public SideBuilder() {
            deck = new ArrayList<>();
            hand = new ArrayList<>();
            handState = new ArrayList<>();
            sentPassives = new ArrayList<>();
        }
    }

    public AbstractGameBuilder(ModelLoader modelLoader) {
        this.modelLoader = modelLoader;
        this.allPassives = modelLoader.getFirstPassives();
        gameStateBuilder = new GameStateBuilder();
        sideBuilderMap = new EnumMap<>(Side.class);
        sideBuilderMap.put(Side.PLAYER_ONE, new SideBuilder());
        sideBuilderMap.put(Side.PLAYER_TWO, new SideBuilder());
    }

    public AbstractGame build() {
        return result;
    }

    protected abstract void build0();

    @Override
    public abstract Response setPassive(Side client, Passive passive, ClientHandler clientHandler);

    @Override
    public abstract Response setDeck(Side client, Deck deck);

    @Override
    public abstract Response selectCard(Side client, int index);

    @Override
    public abstract Response confirm(Side client) throws DatabaseDisconnectException;

    @SuppressWarnings("SameParameterValue")
    protected  <T> List<T> chooseRandom(List<T> list, int n) {
        list = new ArrayList<>(list);
        int k = list.size() - n;
        for (int i = 0; i < k; i++) {
            list.remove((int) (Math.random() * list.size()));
        }
        return list;
    }

    protected List<PassiveOverview> turnToPassiveOverview(List<Passive> passives) {
        List<PassiveOverview> result = new ArrayList<>();
        passives.forEach(p -> result.add(new PassiveOverview(p)));
        return result;
    }

    protected Response sendPassives(Side client, String message) {
        sideBuilderMap.get(client).sentPassives.addAll(chooseRandom(this.allPassives, STARTING_PASSIVES));
        List<PassiveOverview> passives = turnToPassiveOverview(sideBuilderMap.get(client).sentPassives);
        return new PassiveDetails(passives, null, null, message);
    }

    @SuppressWarnings("SameParameterValue")
    protected void pickCards(List<Card> hand, List<Boolean> state, List<Card> deck, int init) {
        for (int i = 0; i < init; i++) {
            int randomIndex = (int) (Math.random() * deck.size());
            hand.add(deck.remove(randomIndex));
            state.add(true);
        }
    }

    protected void deckToList(List<Card> result, Deck d) {
        Map<Card, CardDetails> map = d.getCards();
        for (Card c : map.keySet()) {
            for (int i = 0; i < map.get(c).getRepeatedTimes(); i++) {
                result.add(c);
            }
        }
    }

    protected Response sendCards(List<Card> cards, List<Boolean> states) {
        List<CardOverview> cardOverviews = new ArrayList<>(cards.size());
        for (int i = 0, cardsSize = cards.size(); i < cardsSize; i++) {
            cardOverviews.add(new CardOverview(cards.get(i), states.get(i) ? 1 : 0, false));
        }
        return new PassiveDetails(null, null, cardOverviews
                , "Select initial cards", true);
    }

    protected void finalizeHand(List<Card> hand, List<Boolean> state, List<Card> deck) {
        for (int i = 0, handSize = hand.size(); i < handSize; i++) {
            if (!state.get(i)) {
                int randomIndex = (int) (Math.random() * deck.size());
                Card card = hand.remove(i);
                hand.add(i, deck.remove(randomIndex));
                deck.add(card);
            }
        }
    }

    protected CardOverview changeState(List<Card> hand, List<Boolean> state, int index) {
        state.add(index, !state.remove(index));
        return new CardOverview(hand.get(index), state.get(index) ? 1 : 0, false);
    }

    protected void sendEvents(Side side) {
        List<CardLogic> hand = result.getGameState().getHand(side);
        Collections.reverse(hand);
        hand.forEach(card -> result.getGameState().getEvents().add(
                new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_HAND)
                        .setOverview(new CardOverview(card.getCard())).setSide(side.getIndex()).build()));
        Collections.reverse(hand);
    }
}
