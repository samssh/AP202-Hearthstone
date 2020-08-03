package ir.sam.hearthstone.server.controller.logic;

import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.account.Player;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.client.Overview;
import ir.sam.hearthstone.server.model.client.SmallDeckOverview;
import ir.sam.hearthstone.server.model.log.CollectionLog;
import ir.sam.hearthstone.server.model.main.Card;
import ir.sam.hearthstone.server.model.main.CardDetails;
import ir.sam.hearthstone.server.model.main.ClassOfCard;
import ir.sam.hearthstone.server.model.main.Hero;
import ir.sam.hearthstone.server.model.response.*;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ir.sam.hearthstone.server.controller.Server.MAX_DECK_NUMBER;
import static ir.sam.hearthstone.server.controller.Server.MAX_DECK_SIZE;

public class Collection {
    private final Connector connector;
    private final ModelLoader modelLoader;
    private int mana, lockMode;
    private String name, classOfCard;


    public Collection(Connector connector, ModelLoader modelLoader) {
        this.connector = connector;
        this.modelLoader = modelLoader;
    }

    public Response sendAllCollectionDetails(String name, String classOfCard, int mana,
                                             int lockMode, Player player) {

        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
        Deck d = player.getSelectedDeck();
        List<SmallDeckOverview> decks = makeDeckList(player);
        List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard).orElse(null),
                mana, lockMode, d, player);
        List<CardOverview> deckCards = getDeckCards(d);
        boolean canAddDeck = canAddDeck(player);
        boolean canChangeHero = canChangeHero(d);
        return new AllCollectionDetails(cards, decks, deckCards, canAddDeck, canChangeHero, d.getName()
                , makeHeroNames(player), makeClassOfCardNames());
    }

    public Response applyCollectionFilter(String name, String classOfCard, int mana,
                                          int lockMode, Player player) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
        Deck d = player.getSelectedDeck();
        List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard).orElse(null),
                mana, lockMode, d, player);
        List<CardOverview> deckCards = getDeckCards(d);
        boolean canAddDeck = canAddDeck(player);
        boolean canChangeHero = canChangeHero(d);
        return new CollectionAllCards(cards, deckCards, canAddDeck, canChangeHero, d.getName());
    }

    public Response selectDeck(Player player, String deckName) {
        Optional<Deck> deck = getDeck(deckName, player);
        if (deck.isPresent()) {
            if (player.getSelectedDeckIndex() != player.getDecks().indexOf(deck.get())) {
                player.setSelectedDeckIndex(player.getDecks().indexOf(deck.get()));
                connector.save(player);
                connector.save(new CollectionLog(player.getUserName(), null, null, deckName,
                        "selected deck", null));
                List<CardOverview> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard).orElse(null),
                        mana, lockMode, player.getSelectedDeck(), player);
                return new CollectionAllCards(cards, getDeckCards(deck.get()), canAddDeck(player),
                        canChangeHero(deck.get()), deckName);
            }
        }
        return null;
    }

    private List<String> makeHeroNames(Player player) {
        return player.getHeroes().stream().map(Hero::getName).collect(Collectors.toList());
    }

    private List<String> makeClassOfCardNames() {
        return modelLoader.getClassOfCards().stream().map(ClassOfCard::getHeroName).collect(Collectors.toList());
    }

    private List<CardOverview> makeCardsList(String name, ClassOfCard classOfCard, int mana,
                                             int lockMode, Deck deck, Player player) {
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

    public Optional<Deck> getDeck(String deckName, Player player) {
        return player.getDecks().stream().filter(d -> d.getName().equals(deckName)).findAny();
    }

    private boolean canAddDeck(Player player) {
        return player.getDecks().size() <= MAX_DECK_NUMBER;
    }

    private boolean canChangeHero(Deck deck) {
        if (deck == null)
            return false;
        return deck.getCards().keySet().stream()
                .allMatch(c -> c.getClassOfCard().equals(modelLoader.getClassOfCard("Neutral")
                        .orElseThrow(() -> new NoSuchElementException("Neutral class not loaded"))));
    }

    public Response[] newDeck(String deckName, String heroName, Player player) {
        Response[] responses = new Response[2];
        if (notContainDeckName(deckName, player) && containHero(heroName, player)) {
            Hero hero = modelLoader.getHero(heroName)
                    .orElseThrow(() -> new NoSuchElementException("the saved hero name not exist"));
            Deck newDeck = new Deck(hero, deckName, player);
            player.getDecks().add(newDeck);
            connector.save(player);
            responses[0] = new CollectionDeckEvent("new", new SmallDeckOverview(newDeck));
            connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName,
                    "create deck", null));
            responses[1] = new ShowMessage("deck created");
        } else {
            responses[1] = new ShowMessage("deck not created");
        }
        return responses;
    }

    public boolean containHero(String heroName, Player player) {
        return player.getHeroes().contains(modelLoader.getHero(heroName).orElse(null));
    }

    private boolean notContainDeckName(String deckName, Player player) {
        return player.getDecks().stream().noneMatch(d -> d.getName().equals(deckName));
    }

    public Response[] deleteDeck(String deckName, Player player) {
        Optional<Deck> optionalDeck = getDeck(deckName, player);
        Response[] responses = new Response[2];
        if (optionalDeck.isPresent()) {
            player.getDecks().remove(optionalDeck.get());
            connector.save(player);
            connector.delete(optionalDeck.get());
            responses[0] = new CollectionDeckEvent("delete", null, deckName);
            responses[1] = new ShowMessage("deck deleted");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    deckName, "delete deck", null));
        } else responses[1] = new ShowMessage("deck not deleted");
        return responses;
    }

    public Response[] changeDeckName(String oldDeckName, String newDeckName, Player player) {
        Response[] responses = new Response[2];
        Optional<Deck> optionalDeck = getDeck(oldDeckName, player);
        if (optionalDeck.isPresent() && notContainDeckName(newDeckName, player)) {
            optionalDeck.get().setName(newDeckName);
            connector.save(player);
            responses[0] = new CollectionDeckEvent("change", new SmallDeckOverview(optionalDeck.get())
                    , oldDeckName);
            responses[1] = new ShowMessage("deck name changed");
            connector.save(new CollectionLog(player.getUserName(), null, null,
                    oldDeckName, "change deck name", newDeckName));
        } else responses[1] = new ShowMessage("deck name not changed");
        return responses;
    }

    public Response[] changeHeroDeck(String deckName, String heroName, Player player) {
        Response[] responses = new Response[2];
        Optional<Deck> optionalDeck = getDeck(deckName, player);
        if (optionalDeck.isPresent() && containHero(heroName, player)) {
            Hero h = modelLoader.getHero(heroName).orElse(null);
            if (!optionalDeck.get().getHero().equals(h)) {
                optionalDeck.get().setHero(h);
                connector.save(player);
                responses[0] = new CollectionDeckEvent("change", new SmallDeckOverview(optionalDeck.get()), deckName);
                connector.save(new CollectionLog(player.getUserName(), null, heroName, deckName
                        , "change hero", null));
                responses[1] = new ShowMessage("hero deck changed");
            }
        } else {
            responses[1] = new ShowMessage("hero deck not changed");
        }
        return responses;
    }

    public Response removeCardFromDeck(String cardName, String deckName, Player player) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        Optional<Deck> optionalDeck = getDeck(deckName, player);
        Response response = null;
        if (optionalCard.isPresent() && optionalDeck.isPresent()) {
            optionalDeck.get().removeCard(optionalCard.get());
            List<String> cards = makeCardsList(name, modelLoader.getClassOfCard(classOfCard).orElse(null),
                    mana, lockMode, optionalDeck.get(), player).stream().map(Overview::getName)
                    .collect(Collectors.toList());
            if (cards.contains(cardName)) response = new CollectionCardEvent("move", cardName,
                    canAddDeck(player), canChangeHero(optionalDeck.get()));
            else response = new CollectionCardEvent("remove", cardName, canAddDeck(player)
                    , canChangeHero(optionalDeck.get()));
            connector.save(player);
            connector.save(new CollectionLog(player.getUserName(), cardName, null
                    , deckName, "remove Card", null));
        }
        return response;
    }

    public Response[] addCardToDeck(String cardName, String deckName, Player player, Shop shop) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        Response[] responses = new Response[2];
        if (optionalCard.isPresent()) {
            Map<Card, CardDetails> playerCards = player.getCards();
            if (playerCards.containsKey(optionalCard.get())) {
                Optional<Deck> optionalDeck = getDeck(deckName, player);
                if (optionalDeck.isPresent()) {
                    if (playerCards.get(optionalCard.get()).getRepeatedTimes() >
                            optionalDeck.get().numberOfCard(optionalCard.get())) {
                        if (isForHero(optionalCard.get().getClassOfCard(), optionalDeck.get().getHero())) {
                            if (optionalDeck.get().getSize() < MAX_DECK_SIZE) {
                                optionalDeck.get().addCard(optionalCard.get());
                                connector.save(player);
                                connector.save(new CollectionLog(player.getUserName(), cardName, null
                                        , deckName, "add Card", null));
                                responses[0] = new CollectionCardEvent("add", cardName,
                                        canAddDeck(player), canChangeHero(optionalDeck.get()));
                            } else {
                                responses[1] = new ShowMessage("cant add card to deck\ndeck is full!!!");
                            }
                        } else {
                            responses[1] = new ShowMessage("cant add card to deck\nchange hero of deck");
                        }
                    } else responses[1] = new ShowMessage("cant add card to deck");
                }
            } else {
                if (shop.canBuy(optionalCard.get(), player)) {
                    responses[1] = new GoTo("SHOP", "you can buy this card in shop\ngoto shop?");
                } else {
                    responses[1] = new ShowMessage("you dont have this card\nyou cant buy card");
                }
            }
        } else {
            responses[1] = new ShowMessage("cant add card to deck");
        }
        return responses;
    }

    private boolean isForHero(ClassOfCard classOfCard, Hero hero) {
        if (classOfCard.getHeroName().equals(hero.getName()))
            return true;
        return classOfCard.equals(modelLoader.getClassOfCard("Neutral").orElse(null));
    }

}
