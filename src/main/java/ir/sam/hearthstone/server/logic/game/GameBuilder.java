package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.CardDetails;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.client.resource_manager.ModelLoader;
import ir.sam.hearthstone.response.PassiveDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.client.view.model.CardOverview;
import ir.sam.hearthstone.client.view.model.PassiveOverview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.sam.hearthstone.server.Server.STARTING_PASSIVES;


public abstract class GameBuilder {
    protected AbstractGame result;
    protected final GameStateBuilder gameStateBuilder;
    protected final List<Passive> allPassives;
    protected List<Passive> sentPassives;
    protected final List<Card> handP1, handP2, deckP1, deckP2;
    protected final List<Boolean> handP1state, handP2state;
    protected final ModelLoader modelLoader;
    protected final Server server;


    public GameBuilder(ModelLoader modelLoader, Server server) {
        this.modelLoader = modelLoader;
        this.allPassives = modelLoader.getFirstPassives();
        this.server = server;
        gameStateBuilder = new GameStateBuilder();
        handP1 = new ArrayList<>();
        handP2 = new ArrayList<>();
        deckP1 = new ArrayList<>();
        deckP2 = new ArrayList<>();
        handP1state = new ArrayList<>();
        handP2state = new ArrayList<>();
    }

    public AbstractGame build() {
        return result;
    }

    protected abstract void build0();

    public abstract Response setPassive(Passive passive, Server server);

    public abstract Response setDeckP1(Deck deckP1);

    public abstract Response setDeckP2(Deck deckP2);

    public abstract Response selectCard(int index);

    public abstract Response confirm();

    private <T> List<T> chooseRandom(List<T> list, int n) {
        list = new ArrayList<>(list);
        int k = list.size() - n;
        for (int i = 0; i < k; i++) {
            list.remove((int) (Math.random() * list.size()));
        }
        return list;
    }

    private List<PassiveOverview> turnToPassiveOverview(List<Passive> passives) {
        List<PassiveOverview> result = new ArrayList<>();
        passives.forEach(p -> result.add(new PassiveOverview(p)));
        return result;
    }

    protected Response sendPassives(String message) {
        sentPassives = chooseRandom(this.allPassives, STARTING_PASSIVES);
        List<PassiveOverview> passives = turnToPassiveOverview(sentPassives);
        return new PassiveDetails(passives, null, null, message);
    }

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
}
