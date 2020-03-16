package model;

import hibernate.ManualMapping;

import java.util.ArrayList;
import java.util.List;

public class Models {
    public static List<Minion> minions;
    public static List<Hero> heroes;
    public static List<Spell> spells;
    public static List<Card> cards;
    public static Hero mage;
    public static List<Hero> firstHeros;
    public static List<Card> firstCards;
    public static List<Deck> firstDecks;


    public static void load() {
        minions = ManualMapping.fetchAll(Minion.class);
        heroes = ManualMapping.fetchAll(Hero.class);
        spells = ManualMapping.fetchAll(Spell.class);
        cards = new ArrayList<>();
        cards.addAll(minions);
        cards.addAll(spells);
        for (Hero h : heroes)
            if (h.getName().equals("mage"))
                mage = h;
        firstHeros = new ArrayList<>(heroes);
        firstDecks = new ArrayList<>();
        for (Hero h : firstHeros) {
            firstDecks.add(new Deck(h));
        }
        firstCards = new ArrayList<>();
        firstCards.add(search("polymorph"));
        firstCards.add(search("Arena Fanatic"));
        firstCards.add(search("Spirit Bomb"));
        firstCards.add(search("Arena Patron"));
        firstCards.add(search("Khartut Defender"));
        firstCards.add(search("Wisp"));
        firstCards.add(search("Weaponized Pinata"));
        firstCards.add(search("Deadly Shot"));
        firstCards.add(search("Kobold Sandtrooper"));
        firstCards.add(search("Baron Geddon"));

    }

    public static Card search(String s) {
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }
}
