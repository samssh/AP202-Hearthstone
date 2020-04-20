package model;

import configs.Config;
import configs.ConfigFactory;
import hibernate.Connector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {
    @Getter
    private final List<Hero> heroes;
    @Getter
    private final List<Card> cards;
    @Getter
    private Hero defaultHero;
    private final List<Hero> firstHeroes;
    private final List<Card> firstCards;
    private final List<Deck> firstDecks;

    public ModelLoader(Connector connector) {
        heroes = connector.fetchAll(Hero.class);
        cards = connector.fetchAll(Card.class);
        firstHeroes = new ArrayList<>();
        firstDecks = new ArrayList<>();
        firstCards = new ArrayList<>();
        config(ConfigFactory.getInstance("").getConfig("MODEL_LOADER_CONFIG"));
    }

    public void config(Config config) {
        defaultHero = searchHero(config.getProperty(String.class, "defaultHero"));
        List<String> heroName = config.getPropertyList(String.class, "firstHeroes");
        for (String s : heroName) {
            firstHeroes.add(searchHero(s));
        }
        for (Hero h : firstHeroes) {
            firstDecks.add(new Deck(h));
        }
        List<String> cardsName = config.getPropertyList(String.class, "firstCards");
        for (String s : cardsName) {
            firstCards.add(searchCard(s));
        }
    }

    public List<Deck> getFirstDecks() {
        List<Deck> deckList = new ArrayList<>(firstDecks);
        deckList.replaceAll(Deck::getclone);
        return deckList;
    }

    public List<Hero> getFirstHeroes() {
        return new ArrayList<>(firstHeroes);
    }

    public List<Card> getFirstCards() {
        return new ArrayList<>(firstCards);
    }

    public Card searchCard(String s) {
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }

    public Hero searchHero(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return h;
        return null;
    }

    public Unit searchUnit(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return h;
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }
}
