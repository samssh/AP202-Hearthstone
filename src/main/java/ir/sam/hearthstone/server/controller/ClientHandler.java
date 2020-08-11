package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.Collection;
import ir.sam.hearthstone.server.controller.logic.Shop;
import ir.sam.hearthstone.server.controller.logic.Status;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.api.GameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.parctice.PracticeGameBuilder;
import ir.sam.hearthstone.server.controller.network.CliectDisconnectException;
import ir.sam.hearthstone.server.controller.network.ResponseSender;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.account.Player;
import ir.sam.hearthstone.server.model.client.SmallDeckOverview;
import ir.sam.hearthstone.server.model.log.AccountLog;
import ir.sam.hearthstone.server.model.log.HeaderLog;
import ir.sam.hearthstone.server.model.log.RequestLog;
import ir.sam.hearthstone.server.model.log.ResponseLog;
import ir.sam.hearthstone.server.model.main.Passive;
import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.requests.RequestExecutor;
import ir.sam.hearthstone.server.model.response.*;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_TWO;

public class ClientHandler implements RequestExecutor {
    private final List<Response> responseList;
    private final Connector connector;
    @Getter
    private final ModelLoader modelLoader;
    @Getter
    private Player player;
    @Setter
    private Game game;
    private GameBuilder gameBuilder;
    private final GameLobby gameLobby;
    private final Collection collection;
    private final Shop shop;
    private final Status status;
    @Getter
    private final ResponseSender responseSender;
    private volatile boolean running;
    @Setter
    private Side side;

    public ClientHandler(ResponseSender responseSender, Connector connector, ModelLoader modelLoader, GameLobby gameLobby) {
        this.responseSender = responseSender;
        this.gameLobby = gameLobby;
        responseList = new ArrayList<>(100);
        this.connector = connector;
        this.modelLoader = modelLoader;
        collection = new Collection(connector, modelLoader);
        shop = new Shop(connector, modelLoader);
        status = new Status();
    }

    public void start() {
        running = true;
        new Thread(this::executeRequests).start();
    }

    private void executeRequests() {
        while (running) {
            Request request;
            try {
                request = responseSender.getRequest();
                try {
                    connector.save(new RequestLog(request, (player == null) ? null : player.getUsername()));
                    request.execute(this);
                } catch (DatabaseDisconnectException e) {
                    responseSender.sendResponse(new GoTo("EXIT", "database disconnected"));
                    break;
                }
                responseSender.sendResponse(responseList.toArray(new Response[0]));
                responseList.clear();
            } catch (CliectDisconnectException e) {
                if (game != null) game.endGame(side);
                if (gameBuilder != null && gameBuilder instanceof OnlineGameBuilder)
                    ((OnlineGameBuilder) gameBuilder).cancel();
                break;
            }
        }
        responseSender.close();
    }

    private void addToResponses(Response... responses) throws DatabaseDisconnectException {
        addToResponses(true, responses);
    }

    private void addToResponses(boolean log, Response... responses) throws DatabaseDisconnectException {
        synchronized (responseList) {
            for (Response response : responses)
                if (response != null) {
                    responseList.add(response);
                    if (log) connector.save(new ResponseLog(response, player.getUsername()));
                }
        }
    }

    @Override
    public void login(String username, String password, int mode) throws DatabaseDisconnectException {
        if (mode == 1)
            signIn(username, password);
        if (mode == 2)
            signUp(username, password);
    }

    private void signIn(String userName, String password) throws DatabaseDisconnectException {
        Player fetched = connector.fetch(Player.class, userName);
        if (fetched != null) {
            if (fetched.getPassword().equals(password)) {
                this.player = fetched;
                Response response = new LoginResponse(true, player.getUsername());
                addToResponses(response);
                connector.save(new AccountLog(player.getUsername(), "sign in"));
            } else {
                Response response = new LoginResponse(false, "wrong password");
                addToResponses(false, response);
                connector.save(new ResponseLog(response, fetched.getUsername()));
            }
        } else {
            Response response = new LoginResponse(false, "username not exist");
            addToResponses(false, response);
            connector.save(new ResponseLog(response, null));
        }
    }

    private void signUp(String username, String password) throws DatabaseDisconnectException {
        Player player = connector.fetch(Player.class, username);
        if (player == null) {
            player = new Player(username, password, System.currentTimeMillis(), Constants.STARTING_COINS, 0
                    , modelLoader.getFirstCards(), modelLoader.getFirstHeroes(), modelLoader.getFirstDecks());
            connector.save(new HeaderLog(player.getCreatTime(), player.getUsername(), player.getPassword()));
            connector.save(player);
            this.player = player;
            Response response = new LoginResponse(true, this.player.getUsername());
            addToResponses(response);
            connector.save(new AccountLog(this.player.getUsername(), "sign up"));
        } else {
            Response response = new LoginResponse(false, "username already exist");
            addToResponses(false, response);
            connector.save(new ResponseLog(response, null));
        }
    }

    @Override
    public void logout() throws DatabaseDisconnectException {
        if (this.player != null) {
            connector.save(player);
            connector.save(new AccountLog(player.getUsername(), "logout"));
            addToResponses(new Logout());
            this.player = null;
        }
    }

    @Override
    public void deleteAccount() throws DatabaseDisconnectException {
        if (this.player != null) {
            connector.delete(player);
            connector.save(new AccountLog(player.getUsername(), "delete account"));
            HeaderLog header = connector.fetch(HeaderLog.class, player.getCreatTime());
            header.setDeletedAt(System.currentTimeMillis() + "");
            connector.save(header);
            addToResponses(new Logout());
            this.player = null;
        }
    }

    @Override
    public void shutdown() throws DatabaseDisconnectException {
        running = false;
        Response response = new ShutDown();
        addToResponses(false, response);
        connector.save(new ResponseLog(response, null));
    }

    @Override
    public void sendShop() throws DatabaseDisconnectException {
        addToResponses(shop.sendShop(player));
    }

    @Override
    public void sellCard(String cardName) throws DatabaseDisconnectException {
        addToResponses(shop.sellCard(cardName, player));
    }

    @Override
    public void buyCard(String cardName) throws DatabaseDisconnectException {
        addToResponses(shop.buyCard(cardName, player));
    }

    @Override
    public void sendStatus() throws DatabaseDisconnectException {
        addToResponses(status.sendStatus(player));
    }

    @Override
    public void selectDeck(String deckName) throws DatabaseDisconnectException {
        addToResponses(collection.selectDeck(player, deckName));
    }

    @Override
    public void sendAllCollectionDetails(String name, String classOfCard, int mana, int lockMode) throws DatabaseDisconnectException {
        addToResponses(collection.sendAllCollectionDetails(name, classOfCard, mana, lockMode, player));
    }

    @Override
    public void applyCollectionFilter(String name, String classOfCard, int mana, int lockMode) throws DatabaseDisconnectException {
        addToResponses(collection.applyCollectionFilter(name, classOfCard, mana, lockMode, player));
    }

    @Override
    public void newDeck(String deckName, String heroName) throws DatabaseDisconnectException {
        addToResponses(collection.newDeck(deckName, heroName, player));
    }

    @Override
    public void deleteDeck(String deckName) throws DatabaseDisconnectException {
        addToResponses(collection.deleteDeck(deckName, player));
    }

    @Override
    public void changeDeckName(String oldDeckName, String newDeckName) throws DatabaseDisconnectException {
        addToResponses(collection.changeDeckName(oldDeckName, newDeckName, player));
    }

    @Override
    public void changeHeroDeck(String deckName, String heroName) throws DatabaseDisconnectException {
        addToResponses(collection.changeHeroDeck(deckName, heroName, player));
    }

    @Override
    public void removeCardFromDeck(String cardName, String deckName) throws DatabaseDisconnectException {
        addToResponses(collection.removeCardFromDeck(cardName, deckName, player));
    }

    @Override
    public void addCardToDeck(String cardName, String deckName) throws DatabaseDisconnectException {
        addToResponses(collection.addCardToDeck(cardName, deckName, player, shop));
    }

    @Override
    public void startPlay(String mode) throws DatabaseDisconnectException {
        Response response = null;
        if (canStartGame(player.getSelectedDeck())) {
            switch (mode) {
                case "practice" -> {
                    this.side = PLAYER_ONE;
                    gameBuilder = new PracticeGameBuilder(modelLoader);
                    response = gameBuilder.setDeck(this.side, player.getSelectedDeck());
                }
                case "online" -> {
                    gameBuilder = gameLobby.getGameBuilder(mode, this);
                    response = gameBuilder.setDeck(this.side, player.getSelectedDeck());
                }
                case "tavernBrawl" -> {
                    List<String> names = gameLobby.getGames();
                    if (names.size() == 0) {
                        response = new GoTo("MAIN_MENU", "no game already");
                    } else response = new GameNames(names);
                }
            }
        } else {
            response = new GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        addToResponses(response);
    }

    @Override
    public void selectPlayMode(String modeName) throws DatabaseDisconnectException {
        gameBuilder = gameLobby.getGameBuilder(modeName, this);
        addToResponses(gameBuilder.setDeck(this.side,player.getSelectedDeck()));
    }

    private boolean canStartGame(Deck deck) {
        return deck != null && deck.getSize() == Constants.MAX_DECK_SIZE;
    }

    @Override
    public void selectPassive(String passiveName) throws DatabaseDisconnectException {
        if (gameBuilder != null) {
            Optional<Passive> optionalPassive = modelLoader.getPassive(passiveName);
            if (optionalPassive.isPresent()) {
                addToResponses(gameBuilder.setPassive(this.side, optionalPassive.get(), this));
            }
        }
    }

    public Response sendDecksForSelection(String message) {
        List<SmallDeckOverview> decks = player.getDecks().stream().filter(this::canStartGame)
                .map(SmallDeckOverview::new).collect(Collectors.toList());
        return new PassiveDetails(null, decks, null, message);
    }

    @Override
    public void selectOpponentDeck(String deckName) throws DatabaseDisconnectException {
        if (gameBuilder != null) {
            Optional<Deck> optionalDeck = collection.getDeck(deckName, player);
            if (optionalDeck.isPresent() && canStartGame(optionalDeck.get())) {
                addToResponses(gameBuilder.setDeck(this.side, optionalDeck.get()));
            }
        }
    }

    @Override
    public void selectCadOnPassive(int index) throws DatabaseDisconnectException {
        if (gameBuilder != null)
            addToResponses(gameBuilder.selectCard(this.side, index));
    }

    @Override
    public void confirm() throws DatabaseDisconnectException {
        if (gameBuilder != null) {
            addToResponses(gameBuilder.confirm(this.side));
            game = gameBuilder.build();
        }
    }

    @Override
    public void endTurn() throws DatabaseDisconnectException {
        if (game != null) {
            game.nextTurn(this.side);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectHero(int side) throws DatabaseDisconnectException {
        if (game != null) {
            game.selectHero(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectHeroPower(int side) throws DatabaseDisconnectException {
        if (game != null) {
            game.selectHeroPower(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectMinion(int side, int index, int emptyIndex) throws DatabaseDisconnectException {
        if (game != null) {
            game.selectMinion(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO, index, emptyIndex);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectCardInHand(int side, int index) throws DatabaseDisconnectException {
        if (game != null) {
            game.selectCardInHand(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO, index);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void sendGameEvents() throws DatabaseDisconnectException {
        if (game != null) {
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void checkForOpponent() throws DatabaseDisconnectException {
        if (gameBuilder instanceof OnlineGameBuilder) {
            addToResponses(((OnlineGameBuilder) gameBuilder).check(side));
        }
    }

    @Override
    public void exitGame() throws DatabaseDisconnectException {
        if (game != null) {
            game.endGame(this.side);
            gameBuilder = null;
            game = null;
            addToResponses(new GoTo("MAIN_MENU", null));
            connector.save(player);
        }
    }

    @Override
    public void cancelGame() {
        if (gameBuilder != null && gameBuilder instanceof OnlineGameBuilder) {
            ((OnlineGameBuilder) gameBuilder).cancel();
            gameBuilder = null;
            game = null;
        }
    }
}