package util;

import configs.Config;
import configs.ConfigFactory;
import hibernate.Connector;
import lombok.Getter;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoader {
    @Getter
    private final List<Hero> heroes;
    @Getter
    private final List<Card> cards;
    @Getter
    private final List<ClassOfCard> classOfCards;
    @Getter
    private Hero defaultHero;
    private final List<Hero> firstHeroes;
    private final List<Card> firstCards;
    private final List<Passive> firstPassives;

    public ModelLoader(Connector connector) {
        heroes = connector.fetchAll(Hero.class);
        cards = connector.fetchAll(Card.class);
        classOfCards = connector.fetchAll(ClassOfCard.class);
        firstHeroes = new ArrayList<>();
        firstCards = new ArrayList<>();
        firstPassives = connector.fetchAll(Passive.class);
        config(ConfigFactory.getInstance().getConfig("MODEL_LOADER_CONFIG"),connector);
    }

    public void config(Config config,Connector connector) {
        defaultHero = getHero(config.getProperty(String.class, "defaultHero"));
        List<String> heroName = config.getPropertyList(String.class, "firstHeroes");
        for (String s : heroName) {
            firstHeroes.add(getHero(s));
        }
        List<String> cardsName = config.getPropertyList(String.class, "firstCards");
        for (String s : cardsName) {
            firstCards.add(getCard(s));
        }
    }

    public List<Deck> getFirstDecks() {
        List<Deck> deckList = new ArrayList<>();
        for (Hero hero:firstHeroes) {
            deckList.add(new Deck(hero,"default"));
        }
        return deckList;
    }

    public List<Passive> getFirstPassives() {
        return new ArrayList<>(firstPassives);
    }

    public List<Hero> getFirstHeroes() {
        return new ArrayList<>(firstHeroes);
    }

    public Map<Card, CardDetails> getFirstCards() {
        Map<Card,CardDetails> map = new HashMap<>();
        for (Card card:firstCards) map.put(card,new CardDetails(1));
        System.out.println(map);
        return map;
    }

    public Card getCard(String s) {
        for (Card c : cards)
            if (c.getName().equals(s))
                return c;
        return null;
    }

    public Hero getHero(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return h;
        return null;
    }

    public ClassOfCard getClassOfCard(String name){
        for (ClassOfCard c : classOfCards)
            if (c.getHeroName().equals(name))
                return c;
        return null;
    }

    public Passive getPassive(String name){
        for (Passive p : firstPassives)
            if (p.getName().equals(name))
                return p;
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
