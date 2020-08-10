package ir.sam.hearthstone.server.resource_loader;

import ir.sam.hearthstone.server.model.account.CardDetails;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.ClassOfCard;
import ir.sam.hearthstone.server.model.main.Hero;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.util.hibernate.Connector;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;

import java.util.*;

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

    public ModelLoader(Connector connector) throws DatabaseDisconnectException {
        heroes = connector.fetchAll(Hero.class);
        cards = connector.fetchAll(Card.class);
        classOfCards = connector.fetchAll(ClassOfCard.class);
        firstHeroes = new ArrayList<>();
        firstCards = new ArrayList<>();
        firstPassives = connector.fetchAll(Passive.class);
        config(ConfigFactory.getInstance().getConfig("MODEL_LOADER_CONFIG"));
    }

    public void config(Config config) {
        defaultHero = getHero(config.getProperty(String.class, "defaultHero"))
                .orElseThrow(() -> new NoSuchElementException("hero on config file not exist"));
        List<String> heroName = config.getPropertyList(String.class, "firstHeroes");
        for (String s : heroName) {
            firstHeroes.add(getHero(s)
                    .orElseThrow(() -> new NoSuchElementException("hero on config file not exist")));
        }
        List<String> cardsName = config.getPropertyList(String.class, "firstCards");
        for (String s : cardsName) {
            firstCards.add(getCard(s)
                    .orElseThrow(() -> new NoSuchElementException("card on config file not exist")));
        }
    }

    public List<Deck> getFirstDecks() {
        List<Deck> deckList = new ArrayList<>();
        for (Hero hero : firstHeroes) {
            deckList.add(new Deck(hero, "default"));
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
        Map<Card, CardDetails> map = new HashMap<>();
        for (Card card : firstCards) map.put(card, new CardDetails(1));
        return map;
    }

    public Optional<Card> getCard(String s) {
        for (Card c : cards)
            if (c.getName().equals(s))
                return Optional.of(c);
        return Optional.empty();
    }

    public Optional<Hero> getHero(String s) {
        for (Hero h : heroes)
            if (h.getName().equals(s))
                return Optional.of(h);
        return Optional.empty();
    }

    public Optional<ClassOfCard> getClassOfCard(String name) {
        for (ClassOfCard c : classOfCards)
            if (c.getHeroName().equals(name))
                return Optional.of(c);
        return Optional.empty();
    }

    public Optional<Passive> getPassive(String name) {
        for (Passive p : firstPassives)
            if (p.getName().equals(name))
                return Optional.of(p);
        return Optional.empty();
    }
}
