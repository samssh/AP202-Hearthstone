package server;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Game {
    private final static int STARTING_CART = 3;
    private List<Card> ground, handCard, deck;
    private Passive passive;
    private Hero hero;
    private Weapon activeWeapon;
    private int mana;

    Game(Deck deck, Passive passive) {
        this.passive = passive;
        this.hero = deck.getHero();
        this.ground = new ArrayList<>();
        this.deck = deckToList(deck);
        this.handCard = new ArrayList<>();
        chooseRandom(handCard,this.deck,STARTING_CART);
        this.mana = 1;
    }

    private List<Card> deckToList(Deck d) {
        List<Card> result = new ArrayList<>();
        Map<Card, CardDetails> map = d.getCards();
        for (Card c : map.keySet()) {
            for (int i = 0; i < map.get(c).getRepeatedTimes(); i++) {
                result.add(c);
            }
        }
        return result;
    }

    private <T> void chooseRandom(List<T> result ,List<T> list, int n) {
        for (int i = 0; i < n; i++) {
            result.add(list.remove((int) (Math.random() * list.size())));
        }
    }

}

