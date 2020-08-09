package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.Collection;
import ir.sam.hearthstone.server.controller.logic.Shop;
import ir.sam.hearthstone.server.controller.logic.Status;
import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.api.GameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.parctice.PracticeGameBuilder;
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
    private Player player;
    @Setter
    private Game game;
    private GameBuilder gameBuilder;
    private final Collection collection;
    private final Shop shop;
    private final Status status;
    @Getter
    private final ResponseSender responseSender;
    private volatile boolean running;
    private Side side;

    public ClientHandler(ResponseSender responseSender, Connector connector, ModelLoader modelLoader) {
        this.responseSender = responseSender;
        responseList = new ArrayList<>(100);
        this.connector = connector;
        this.modelLoader = modelLoader;
        collection = new Collection(connector, modelLoader);
        shop = new Shop(connector, modelLoader);
        status = new Status();
    }

    public ClientHandler start() {
        running = true;
        new Thread(this::executeRequests).start();
        return this;
    }

    private void executeRequests() {
        while (running) {
            Request request = responseSender.getRequest();
            connector.save(new RequestLog(request, (player == null) ? null : player.getUsername()));
            request.execute(this);
            responseSender.sendResponse(responseList.toArray(new Response[0]));
            responseList.clear();
        }
        responseSender.close();
    }

    private void addToResponses(Response... responses) {
        addToResponses(true, responses);
    }

    private void addToResponses(boolean log, Response... responses) {
        synchronized (responseList) {
            for (Response response : responses)
                if (response != null) {
                    responseList.add(response);
                    if (log) connector.save(new ResponseLog(response, player.getUsername()));
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

    private void signUp(String username, String password) {
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
    public void logout() {
        if (this.player != null) {
            connector.save(player);
            connector.save(new AccountLog(player.getUsername(), "logout"));
            addToResponses(new Logout());
            this.player = null;
        }
    }

    @Override
    public void deleteAccount() {
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
    public void shutdown() {
        running = false;
        Response response = new ShutDown();
        addToResponses(false, response);
        connector.save(new ResponseLog(response, null));
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
    public void startPlay(String mode) {
        Response response = null;
        if (canStartGame(player.getSelectedDeck())) {
            switch (mode) {
                case "practice" -> {
                    this.side = PLAYER_ONE;
                    gameBuilder = new PracticeGameBuilder(modelLoader);
                    response = gameBuilder.setDeck(this.side, player.getSelectedDeck());
                }
                case "online" -> response = new ShowMessage("online add soon");
                case "tavernBrawl" -> response = new ShowMessage("tavernBrawl add soon");
            }
        } else {
            response = new GoTo("COLLECTION", "your deck is not ready\ngoto collection?");
        }
        addToResponses(response);
    }

    @Override
    public void selectPlayMode(String modeName) {
        //tavernBrawl
    }

    private boolean canStartGame(Deck deck) {
        return deck != null && deck.getSize() == Constants.MAX_DECK_SIZE;
    }

    @Override
    public void selectPassive(String passiveName) {
        if (gameBuilder != null) {
            Optional<Passive> optionalPassive = modelLoader.getPassive(passiveName);
            optionalPassive.ifPresent(passive -> addToResponses(gameBuilder.setPassive(this.side, passive, this)));
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
                addToResponses(gameBuilder.setDeck(this.side, optionalDeck.get()));
            }
        }
    }

    @Override
    public void selectCadOnPassive(int index) {
        if (gameBuilder != null)
            addToResponses(gameBuilder.selectCard(this.side, index));
    }

    @Override
    public void confirm() {
        if (gameBuilder != null) {
            addToResponses(gameBuilder.confirm(this.side));
            game = gameBuilder.build();
        }
    }

    @Override
    public void endTurn() {
        if (game != null) {
            game.nextTurn(this.side);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectHero(int side) {
        if (game != null) {
            game.selectHero(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectHeroPower(int side) {
        if (game != null) {
            game.selectHeroPower(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectMinion(int side, int index, int emptyIndex) {
        if (game != null) {
            game.selectMinion(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO, index, emptyIndex);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void selectCardInHand(int side, int index) {
        if (game != null) {
            game.selectCardInHand(this.side, side == 0 ? PLAYER_ONE : PLAYER_TWO, index);
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void sendGameEvents() {
        if (game!=null){
            addToResponses(game.getResponse(this.side));
        }
    }

    @Override
    public void checkForOpponent() {
        if(gameBuilder instanceof OnlineGameBuilder){
            addToResponses(((OnlineGameBuilder) gameBuilder).check(side));
        }
    }

    @Override
    public void exitGame() {
        if (game != null) {
            game.endGame(this.side);
            gameBuilder = null;
            game = null;
            addToResponses(new GoTo("MAIN_MENU", null));
        }
    }
}