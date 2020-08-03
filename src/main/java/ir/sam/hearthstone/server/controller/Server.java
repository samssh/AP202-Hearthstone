package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.Collection;
import ir.sam.hearthstone.server.controller.logic.Shop;
import ir.sam.hearthstone.server.controller.logic.Status;
import ir.sam.hearthstone.server.controller.logic.game.AbstractGame;
import ir.sam.hearthstone.server.controller.logic.game.GameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.MultiplayerGameBuilder;
import ir.sam.hearthstone.server.controller.transmitters.ResponseSender;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.account.Player;
import ir.sam.hearthstone.server.model.client.SmallDeckOverview;
import ir.sam.hearthstone.server.model.log.AccountLog;
import ir.sam.hearthstone.server.model.log.HeaderLog;
import ir.sam.hearthstone.server.model.log.ResponseLog;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.requests.RequestExecutor;
import ir.sam.hearthstone.server.model.response.GoTo;
import ir.sam.hearthstone.server.model.response.LoginResponse;
import ir.sam.hearthstone.server.model.response.PassiveDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.Config;
import ir.sam.hearthstone.server.resource_loader.ConfigFactory;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_TWO;

public class Server implements RequestExecutor {
    public final static int STARTING_MANA;
    public final static int MANA_PER_TURN;
    public final static int CARD_PER_TURN;
    public final static int MAX_DECK_SIZE;
    public final static int STARTING_PASSIVES;
    public final static int STARTING_HAND_CARDS;
    public final static int MAX_MANA;
    public final static int STARTING_COINS;
    public final static int MAX_DECK_NUMBER;
    public final static int TURN_TIME;// to millisecond
    public final static int MAX_HAND_SIZE;
    public final static int MAX_GROUND_SIZE;

    static {
        Config config = ConfigFactory.getInstance().getConfig("SERVER_CONFIG");
        STARTING_MANA = config.getProperty(Integer.class, "STARTING_MANA");
        MANA_PER_TURN = config.getProperty(Integer.class, "MANA_PER_TURN");
        CARD_PER_TURN = config.getProperty(Integer.class, "CARD_PER_TURN");
        MAX_DECK_SIZE = config.getProperty(Integer.class, "MAX_DECK_SIZE");
        STARTING_PASSIVES = config.getProperty(Integer.class, "STARTING_PASSIVES");
        STARTING_HAND_CARDS = config.getProperty(Integer.class, "STARTING_HAND_CARDS");
        MAX_MANA = config.getProperty(Integer.class, "MAX_MANA");
        STARTING_COINS = config.getProperty(Integer.class, "STARTING_COINS");
        MAX_DECK_NUMBER = config.getProperty(Integer.class, "MAX_DECK_NUMBER");
        TURN_TIME = config.getProperty(Integer.class, "TURN_TIME");
        MAX_HAND_SIZE = config.getProperty(Integer.class, "MAX_HAND_SIZE");
        MAX_GROUND_SIZE = config.getProperty(Integer.class, "MAX_GROUND_SIZE");
    }

    private final List<Response> responseList;
    private final Connector connector;
    @Getter
    private final ModelLoader modelLoader;
    private Player player;
    private AbstractGame game;
    private GameBuilder gameBuilder;
    private final Collection collection;
    private final Shop shop;
    private final Status status;
    private final ResponseSender responseSender;
    private volatile boolean running;

    public Server(ResponseSender responseSender) {
        this.responseSender = responseSender;
        responseList = new ArrayList<>(100);
        connector = new Connector(ConfigFactory.getInstance().getConfigFile("SERVER_HIBERNATE_CONFIG")
                , System.getenv("HearthStone password"));
        modelLoader = new ModelLoader(connector);
        collection = new Collection(connector, modelLoader);
        shop = new Shop(connector, modelLoader);
        status = new Status();
    }

    public void start(){
        running = true;
        new Thread(this::executeRequests).start();
    }

    private void executeRequests() {
        while (running) {
            Request request = responseSender.getRequest();
            request.execute(this);
            responseSender.sendResponse(responseList.toArray(new Response[0]));
            responseList.clear();
        }
    }

    @Override
    public void shutdown() {
        running = false;
        connector.close();
    }

    private void addToResponses(Response... responses) {
        addToResponses(true, responses);
    }

    private void addToResponses(boolean log, Response... responses) {
        synchronized (responseList) {
            for (Response response : responses)
                if (response != null) {
                    responseList.add(response);
                    if (log) connector.save(new ResponseLog(response, player.getUserName()));
                }
        }
    }

    @Override
    public void login(String username, String password, int mode) {
        if (mode == 1)
            signIn(username, password);
        if (mode == 2)
            signUp(username, password);
    }

    private void signIn(String userName, String password) {
        Player fetched = connector.fetch(Player.class, userName);
        if (fetched != null) {
            if (fetched.getPassword().equals(password)) {
                this.player = fetched;
                Response response = new LoginResponse(true, player.getUserName());
                addToResponses(response);
                connector.save(new ResponseLog(response, player.getUserName()));
                connector.save(new AccountLog(player.getUserName(), "sign in"));
            } else {
                Response response = new LoginResponse(false, "wrong password");
                addToResponses(false, response);
                connector.save(new ResponseLog(response, fetched.getUserName()));
            }
        } else {
            Response response = new LoginResponse(false, "username not exist");
            addToResponses(false, response);
            connector.save(new ResponseLog(response, null));
        }
    }

    private void signUp(String username, String password) {
        Player player = connector.fetch(Player.class, username);
        if (player == null) {
            player = new Player(username, password, System.currentTimeMillis(), STARTING_COINS, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            connector.save(new HeaderLog(player.getCreatTime(), player.getUserName(), player.getPassword()));
            connector.save(player);
            this.player = player;
            Response response = new LoginResponse(true, this.player.getUserName());
            addToResponses(response);
            connector.save(new AccountLog(this.player.getUserName(), "sign up"));
        } else {
            Response response = new LoginResponse(false, "username already exist");
            addToResponses(false, response);
            connector.save(new ResponseLog(response, null));
        }
    }

    @Override
    public void logout() {
        if (this.player != null) {
            connector.save(player);
            connector.save(new AccountLog(player.getUserName(), "logout"));
            this.player = null;
        }
    }

    @Override
    public void deleteAccount() {
        if (this.player != null) {
            connector.delete(player);
            connector.save(new AccountLog(player.getUserName(), "delete account"));
            HeaderLog header = connector.fetch(HeaderLog.class, player.getCreatTime());
            header.setDeletedAt(System.currentTimeMillis() + "");
            connector.save(header);
            this.player = null;
        }
    }

    @Override
    public void sendShop() {
        addToResponses(shop.sendShop(player));
    }

    @Override
    public void sellCard(String cardName) {
        addToResponses(shop.sellCard(cardName, player));
    }

    @Override
    public void buyCard(String cardName) {
        addToResponses(shop.buyCard(cardName, player));
    }

    @Override
    public void sendStatus() {
        addToResponses(status.sendStatus(player));
    }

    @Override
    public void selectDeck(String deckName) {
        addToResponses(collection.selectDeck(player, deckName));
    }

    @Override
    public void sendAllCollectionDetails(String name, String classOfCard, int mana, int lockMode) {
        addToResponses(collection.sendAllCollectionDetails(name, classOfCard, mana, lockMode, player));
    }

    @Override
    public void applyCollectionFilter(String name, String classOfCard, int mana, int lockMode) {
        addToResponses(collection.applyCollectionFilter(name, classOfCard, mana, lockMode, player));
    }

    @Override
    public void newDeck(String deckName, String heroName) {
        addToResponses(collection.newDeck(deckName, heroName, player));
    }

    @Override
    public void deleteDeck(String deckName) {
        addToResponses(collection.deleteDeck(deckName, player));
    }

    @Override
    public void changeDeckName(String oldDeckName, String newDeckName) {
        addToResponses(collection.changeDeckName(oldDeckName, newDeckName, player));
    }

    @Override
    public void changeHeroDeck(String deckName, String heroName) {
        addToResponses(collection.changeHeroDeck(deckName, heroName, player));
    }

    @Override
    public void removeCardFromDeck(String cardName, String deckName) {
        addToResponses(collection.removeCardFromDeck(cardName, deckName, player));
    }

    @Override
    public void addCardToDeck(String cardName, String deckName) {
        addToResponses(collection.addCardToDeck(cardName, deckName, player, shop));
    }

    @Override
    public void startPlay() {
        Response response;
        if (canStartGame(player.getSelectedDeck())) {
            response = new GoTo("PLAY_MODE", null);
        } else {
            response = new GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        addToResponses(response);
    }

    @Override
    public void selectPlayMode(String modeName) {
        Response response = null;
        switch (modeName) {
            case "multiplayer" -> {
                gameBuilder = new MultiplayerGameBuilder(modelLoader, this);
                response = gameBuilder.setDeckP1(player.getSelectedDeck());
            }
            case "AI" -> response = new GoTo("MAIN_MENU", "AI add soon\ngoto main menu?");
            case "deck reader" -> response = new GoTo("MAIN_MENU", "deck reader add soon\ngoto main menu?");
            case "online" -> response = new GoTo("MAIN_MENU", "online add in next phase\ngoto main menu?");
        }
        addToResponses(response);
    }

    private boolean canStartGame(Deck deck) {
        return deck != null && deck.getSize() == MAX_DECK_SIZE;
    }

    @Override
    public void selectPassive(String passiveName) {
        if (gameBuilder != null) {
            Optional<Passive> optionalPassive = modelLoader.getPassive(passiveName);
            optionalPassive.ifPresent(passive -> addToResponses(gameBuilder.setPassive(passive, this)));
        }
    }

    public Response sendDecksForSelection(String message) {
        List<SmallDeckOverview> decks = player.getDecks().stream().filter(this::canStartGame)
                .map(SmallDeckOverview::new).collect(Collectors.toList());
        return new PassiveDetails(null, decks, null, message);
    }

    @Override
    public void selectOpponentDeck(String deckName) {
        if (gameBuilder != null) {
            Optional<Deck> optionalDeck = collection.getDeck(deckName, player);
            if (optionalDeck.isPresent() && canStartGame(optionalDeck.get())) {
                addToResponses(gameBuilder.setDeckP2(optionalDeck.get()));
            }
        }
    }

    @Override
    public void selectCadOnPassive(int index) {
        if (gameBuilder != null)
            addToResponses(gameBuilder.selectCard(index));
    }

    @Override
    public void confirm() {
        if (gameBuilder != null) {
            addToResponses(gameBuilder.confirm());
            game = gameBuilder.build();
        }
    }

    @Override
    public void endTurn() {
        if (game != null) {
            game.nextTurn(PLAYER_ONE);
            addToResponses(game.getResponse(PLAYER_ONE));
        }
    }

    @Override
    public void selectHero(int side) {
        if (game != null) {
            game.selectHero(PLAYER_ONE, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(PLAYER_ONE));
        }
    }

    @Override
    public void selectHeroPower(int side) {
        if (game != null) {
            game.selectHeroPower(PLAYER_ONE, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(PLAYER_ONE));
        }
    }

    @Override
    public void selectMinion(int side, int index, int emptyIndex) {
        if (game != null) {
            game.selectMinion(PLAYER_ONE, side == 0 ? PLAYER_ONE : PLAYER_TWO, index, emptyIndex);
            addToResponses(game.getResponse(PLAYER_ONE));
        }
    }

    @Override
    public void selectCardInHand(int side, int index) {
        if (game != null) {
            game.selectCardInHand(PLAYER_ONE, side == 0 ? PLAYER_ONE : PLAYER_TWO, index);
            addToResponses(game.getResponse(PLAYER_ONE));
        }
    }

    @Override
    public void exitGame() {
        game.getTimer().cancelTask();
        gameBuilder = null;
        game = null;
        addToResponses(new GoTo("MAIN_MENU", null));
    }
}