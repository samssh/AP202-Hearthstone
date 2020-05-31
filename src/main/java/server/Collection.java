package server;

import client.Client;
import hibernate.Connector;
import model.account.*;
import model.log.*;
import model.main.*;
import response.*;
import util.ModelLoader;
import view.model.CardOverview;
import view.model.SmallDeckOverview;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static server.Server.*;

public class Collection {
    private final Connector connector;
    private final ModelLoader modelLoader;
    private int mana, lockMode;
    private String name, classOfCard;
    private String deckName;

    public Collection(Connector connector, ModelLoader modelLoader) {
        this.connector = connector;
        this.modelLoader = modelLoader;
    }

    public void sendFirstCollection(Player player) {
        Response response = new FirstCollectionDetails(makeHeroNames(player), makeClassOfCardNames());
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    private List<String> makeHeroNames(Player player) {
        return player.getHeroes().stream().map(Hero::getName).collect(Collectors.toList());
    }

    private List<String> makeClassOfCardNames() {
        return modelLoader.getClassOfCards().stream().map(ClassOfCard::getHeroName).collect(Collectors.toList());
    }

    public void sendCollectionDetails(String name, String classOfCard, int mana,
                                      int lockMode, String deckName, Player player) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
        this.deckName = deckName;
        List<SmallDeckOverview> decks = makeDeckList(player);
        Deck d = getDeck(deckName, player);
        if (d != null) {
            if (player.getSelectedDeckIndex() != player.getDecks().indexOf(d)) {
                player.setSelectedDeckIndex(player.getDecks().indexOf(d));
                connector.save(player);
                connector.save(new CollectionLog(player.getUserName(), null, null, deckName,
                        "selected deck", null));
            }
        }
        List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard).orElse(null), mana, lockMode, d, player);
        List<CardOverview> deckCards = getDeckCards(d);
        boolean canAddDeck = canAddDeck(player);
        boolean canChangeHero = canChangeHero(d);
        Response response = new CollectionDetails(cards, decks, deckCards, canAddDeck, canChangeHero, deckName);
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    private List<CardOverview> makeCardsList(String name, ClassOfCard classOfCard, int mana, int lockMode, Deck deck, Player player) {
        List<Card> result = new ArrayList<>(modelLoader.getCards());
        Stream<Card> cardStream = result.stream();
        if (name != null) cardStream = cardStream.filter(c -> c.getName().toLowerCase().contains(name));
        if (mana != 0) cardStream = cardStream.filter(c -> c.getManaFrz() == mana);
        if (classOfCard != null) cardStream = cardStream.filter(c -> c.getClassOfCard().equals(classOfCard));
        return cardStream.map(c -> filterLockMode(c, lockMode, deck, player))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private CardOverview filterLockMode(Card card, int lockMode, Deck deck, Player player) {
        CardOverview result = null;
        Map<Card, CardDetails> map = player.getCards();
        Map<Card, CardDetails> deckMap = deck != null ? deck.getCards() : Collections.emptyMap();
        boolean locked = lockMode != 2, unlocked = lockMode != 1;
        if (map.containsKey(card)) {
            if (deckMap.containsKey(card)) {
                int r = map.get(card).getRepeatedTimes() - deckMap.get(card).getRepeatedTimes();
                if (r > 0)
                    if (unlocked) result = new CardOverview(card, r, false);
            } else if (unlocked) result = new CardOverview(card, map.get(card).getRepeatedTimes(), false);
        } else {
            if (locked) result = new CardOverview(card, 0, false);
        }
        return result;
    }

    private List<SmallDeckOverview> makeDeckList(Player player) {
        return player.getDecks().stream().map(SmallDeckOverview::new).collect(Collectors.toList());
    }

    private List<CardOverview> getDeckCards(Deck deck) {
        if (deck == null)
            return null;
        return deck.getCards().keySet().stream().
                map(c -> new CardOverview(c, deck.getCards().get(c).getRepeatedTimes(), false))
                .collect(Collectors.toList());
    }

    private Deck getDeck(String deckName, Player player) {
        return player.getDecks().stream().filter(d -> d.getName().equals(deckName)).findAny().orElse(null);
    }

    private boolean canAddDeck(Player player) {
        return player.getDecks().size() <= MAX_DECK_NUMBER;
    }

    private boolean canChangeHero(Deck deck) {
        if (deck == null)
            return false;
        return deck.getCards().keySet().stream()
                .anyMatch(c -> !c.getClassOfCard().equals(modelLoader.getClassOfCard("Neutral")
                        .orElseThrow(NullPointerException::new)));
    }

    public void newDeck(String deckName, String heroName, Player player) {
        Response response;
        if (!containDeckName(deckName,player) && containHero(heroName,player)) {
            Hero h = modelLoader.getHero(heroName).orElse(null);
            player.getDecks().add(new Deck(h, deckName, player));
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, this.deckName,player);
            connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName,
                    "create deck", null));
            response = new showMessage("deck created");
        } else {
            response = new showMessage("deck not created");
        }
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    public boolean containHero(String heroName,Player player) {
        return player.getHeroes().contains(modelLoader.getHero(heroName).orElse(null));
    }

    private boolean containDeckName(String deckName,Player player) {
        return player.getDecks().stream().anyMatch(d -> d.getName().equals(deckName));
    }

    public void deleteDeck(String deckName,Player player) {
        Deck deck = getDeck(deckName,player);
        Response response;
        if (deck != null) {
            player.getDecks().remove(deck);
            connector.save(player);
            connector.delete(deck);
            sendCollectionDetails(name, classOfCard, mana, lockMode, null,player);
            response = new showMessage("deck deleted");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    deckName, "delete deck", null));
        } else response = new showMessage("deck not deleted");
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    public void changeDeckName(String oldDeckName, String newDeckName,Player player) {
        Response response;
        if (containDeckName(oldDeckName,player) && !containDeckName(newDeckName,player)) {
            Deck deck = getDeck(oldDeckName,player);
            Objects.requireNonNull(deck).setName(newDeckName);
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, newDeckName,player);
            response = new showMessage("deck name changed");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    oldDeckName, "change deck name", newDeckName));
        } else response = new showMessage("deck name not changed");
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    public void changeHeroDeck(String deckName, String heroName,Player player) {
        Response response;
        if (containDeckName(deckName,player) && containHero(heroName,player)) {
            Hero h = modelLoader.getHero(heroName).orElse(null);
            Deck deck = getDeck(deckName,player);
            Objects.requireNonNull(deck).setHero(h);
            connector.save(player);
            sendCollectionDetails(name, classOfCard, mana, lockMode, deckName,player);
            connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName
                    , "change hero", null));
            response = new showMessage("hero deck changed");
        } else {
            response = new showMessage("hero deck not changed");
        }
        Client.getInstance().putAnswer(response);
        connector.save(new ResponseLog(response, player.getUserName()));
    }

    public void removeCardFromDeck(String cardName, String deckName,Player player) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        Deck deck = getDeck(deckName,player);
        if (optionalCard.isPresent() && deck != null) {
            deck.removeCard(optionalCard.get());
            sendCollectionDetails(name, classOfCard, mana, lockMode, deckName,player);
            connector.save(player);
            connector.save(new CollectionLog(player.getUserName(), cardName, null
                    , deckName, "remove Card", null));
        }
    }

    public void addCardToDeck(String cardName, String deckName,Player player,Shop shop) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        Response response = null;
        if (optionalCard.isPresent()) {
            Map<Card, CardDetails> playerCards = player.getCards();
            if (playerCards.containsKey(optionalCard.get())) {
                Deck deck = getDeck(deckName,player);
                if (deck != null) {
                    if (playerCards.get(optionalCard.get()).getRepeatedTimes() > deck.numberOfCard(optionalCard.get())) {
                        if (isForHero(optionalCard.get().getClassOfCard(), deck.getHero())) {
                            if (deck.getSize() < MAX_DECK_SIZE) {
                                deck.addCard(optionalCard.get());
                                connector.save(player);
                                connector.save(new CollectionLog(player.getUserName(), cardName, null
                                        , deckName, "add Card", null));
                                sendCollectionDetails(name, classOfCard, mana, lockMode, deckName,player);

                            } else {
                                response = new showMessage("cant add card to deck\ndeck is full!!!");
                            }
                        } else {
                            response = new showMessage("cant add card to deck\nchange hero of deck");
                        }
                    } else response = new showMessage("cant add card to deck");
                }
            } else {
                if (shop.canBuy(optionalCard.get(),player)) {
                    response = new GoTo("SHOP", "you can buy this card in shop\ngoto shop?");
                } else {
                    response = new showMessage("you dont have this card\nyou cant buy card");
                }
            }
        } else {
            response = new showMessage("cant add card to deck");
        }
        Client.getInstance().putAnswer(response);
        if (response != null)
            connector.save(new ResponseLog(response, player.getUserName()));
    }

    private boolean isForHero(ClassOfCard classOfCard, Hero hero) {
        if (classOfCard.getHeroName().equals(hero.getName()))
            return true;
        return classOfCard.equals(modelLoader.getClassOfCard("Neutral").orElse(null));
    }

}
