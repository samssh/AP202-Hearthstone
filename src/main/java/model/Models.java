package model;

import hibernate.Connector;
import java.util.ArrayList;
import java.util.List;

public class Models {
    public static List<Hero> heroes;
    public static List<Card> cards;
    public static Hero mage;
    public static Deck mageDeck;
    public static List<Hero> firstHeros;
    public static List<Card> firstCards;
    public static List<Deck> firstDecks;


    public static void load() {
        Connector connector=Connector.getConnector();
        heroes = connector.fetchAll(Hero.class);
        cards = connector.fetchAll(Card.class);
        for (Hero h : heroes)
            if (h.getName().equals("Mage"))
                mage = h;
        firstHeros = new ArrayList<>();
        firstHeros.add(mage);
        firstDecks = new ArrayList<>();
        for (Hero h : firstHeros) {
            firstDecks.add(new Deck(h));
            if (h.getName().equals("Mage"))
                mageDeck = firstDecks.get(firstDecks.size() - 1);
        }
        firstCards = new ArrayList<>();
        firstCards.add(searchCard("polymorph"));
        firstCards.add(searchCard("Arena Fanatic"));
        firstCards.add(searchCard("Spirit Bomb"));
        firstCards.add(searchCard("Arena Patron"));
        firstCards.add(searchCard("Khartut Defender"));
        firstCards.add(searchCard("Wisp"));
        firstCards.add(searchCard("Weaponized Pinata"));
        firstCards.add(searchCard("Deadly Shot"));
        firstCards.add(searchCard("Kobold Sandtrooper"));
        firstCards.add(searchCard("Baron Geddon"));

    }

    public static Card searchCard(String s) {
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }

    public static Hero searchHero(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return h;
        return null;
    }

    public static Unit searchUnit(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return h;
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }
}
