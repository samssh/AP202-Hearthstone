package ir.sam.hearthstone.client;

import ir.sam.hearthstone.Transmitters.RequestSender;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.*;
import ir.sam.hearthstone.requests.*;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.util.Loop;
import ir.sam.hearthstone.util.Updatable;
import ir.sam.hearthstone.view.MyFrame;
import ir.sam.hearthstone.view.PanelType;
import ir.sam.hearthstone.view.model.*;
import ir.sam.hearthstone.view.panel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.awt.event.ItemEvent;
import java.util.*;

import static ir.sam.hearthstone.view.PanelType.*;

public class Client {
    private final JFrame frame;
    private final Map<PanelType, JPanel> panels;
    private final Stack<PanelType> history;
    private PanelType now;
    private final Connector connector;
    private final List<Response> tempResponseList, responseList;
    private final Loop executor;
    private String username;
    //    @Setter
    private final RequestSender requestSender;

    public Client(RequestSender requestSender) {
        this.requestSender = requestSender;
        this.frame = new MyFrame();
        panels = new HashMap<>();
        history = new Stack<>();
        panels.put(PanelType.LOGIN, new LoginPanel(new LoginPanelAction()));
        now = PanelType.LOGIN;
        updateFrame();
        panels.put(PanelType.MAIN_MENU, new MainMenuPanel(new MainMenuAction()));
        panels.put(PanelType.SHOP, new ShopPanel(new ShopAction()));
        panels.put(PanelType.STATUS, new StatusPanel(new StatusAction()));
        panels.put(PanelType.COLLECTION, new CollectionPanel(new CollectionAction()));
        panels.put(PanelType.PASSIVE, new PassivePanel(new PassiveAction()));
        panels.put(PanelType.PLAY, new PlayPanel(new PlayAction()));
        tempResponseList = new LinkedList<>();
        responseList = new LinkedList<>();
        executor = new Loop(60, this::executeAnswers);
        executor.start();
        connector = new Connector(ConfigFactory.getInstance().getConfigFile("CLIENT_HIBERNATE_CONFIG"));
    }

    private void executeAnswers() {
        synchronized (tempResponseList) {
            responseList.addAll(tempResponseList);
            tempResponseList.clear();
        }
        responseList.forEach(response -> response.execute(this));
        responseList.clear();
    }

    public void addResponse(Response response) {
        if (response != null) {
            synchronized (tempResponseList) {
                tempResponseList.add(response);
            }
            connector.save(new ResponseLog(response, username));
        }
    }

    private void shutdown() {
        connector.close();
        executor.stop();
    }

    private void updateFrame() {
        frame.setContentPane(panels.get(now));
        history.push(now);
    }

    private void exit() {
        requestSender.sendRequest(new ShutdownRequest());
        this.shutdown();
        System.exit(0);
    }

    public void login(boolean success, String message) {
        LoginPanel panel = (LoginPanel) panels.get(PanelType.LOGIN);
        if (success) {
            panel.reset();
            now = PanelType.MAIN_MENU;
            updateFrame();
            username = message;
            ((MainMenuPanel) panels.get(PanelType.MAIN_MENU)).update();
        } else {
            panel.setMessage(message);
        }
    }

    private void logout() {
        Request request = new LogoutRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void deleteAccount() {
        Request request = new DeleteAccount();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void back() {
        history.pop();
        now = history.peek();
        if (panels.get(now) instanceof Updatable)
            ((Updatable) panels.get(now)).update();
        frame.setContentPane(panels.get(now));
    }

    private void backMainMenu() {
        while (history.peek() != PanelType.MAIN_MENU)
            history.pop();
        now = history.peek();
        frame.setContentPane(panels.get(now));
    }

    private void sendLoginRequest(String username, String pass, int mode) {
        Request request = new LoginRequest(username, pass, mode);
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void sendShopRequest() {
        Request request = new ShopRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void sendStatusRequest() {
        Request request = new StatusRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void sendFirstCollectionRequest() {
        Request request = new FirstCollection();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    private void sendCollectionRequest(String name, String classOfCard, int mana, int lockMode, String deckName) {
        Request request = new CollectionDetails(name, classOfCard, mana, lockMode, deckName);
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void setShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coin) {
        if (now != PanelType.SHOP) {
            now = PanelType.SHOP;
            updateFrame();
        }
        ShopPanel shopPanel = (ShopPanel) panels.get(PanelType.SHOP);
        shopPanel.setSell(sell);
        shopPanel.setBuy(buy);
        shopPanel.setCoin(coin);
    }

    public void setStatusDetails(List<BigDeckOverview> bigDeckOverviews) {
        StatusPanel statusPanel = (StatusPanel) panels.get(PanelType.STATUS);
        statusPanel.setDeckBoxList(bigDeckOverviews);
        if (now != PanelType.STATUS) {
            now = PanelType.STATUS;
            updateFrame();
        }
    }

    public void setFirstCollectionDetail(List<String> heroNames, List<String> classOfCardNames) {
        CollectionPanel collectionPanel = (CollectionPanel) panels.get(PanelType.COLLECTION);
        collectionPanel.setFirstDetails(heroNames, classOfCardNames);
        if (now != PanelType.COLLECTION) {
            now = PanelType.COLLECTION;
            updateFrame();
        }
    }

    public void setCollectionDetail(List<CardOverview> cards, List<SmallDeckOverview> decks,
                                    List<CardOverview> deckCards, boolean canAddDeck,
                                    boolean canChangeHero, String deckName) {
        CollectionPanel collectionPanel = (CollectionPanel) panels.get(PanelType.COLLECTION);
        collectionPanel.setDetails(cards, decks, deckCards, canAddDeck, canChangeHero, deckName);
        if (now != PanelType.COLLECTION) {
            now = PanelType.COLLECTION;
            updateFrame();
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public void goTo(String panel, String message) {
        try {
            PanelType p = PanelType.valueOf(panel);
            boolean flag = message == null || JOptionPane.showConfirmDialog(frame, message, "goto",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if (flag) {
                now = p;
                updateFrame();
                if (panels.get(now) instanceof Updatable)
                    ((Updatable) panels.get(now)).update();
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void sendStartPlayRequest() {
        Request request = new StartPlaying();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void setPassives(List<PassiveOverview> passives) {
        ((PassivePanel) panels.get(PanelType.PASSIVE)).setPassives(passives);
        if (now != PanelType.PASSIVE) {
            now = PanelType.PASSIVE;
            updateFrame();
        }
    }

    public void setPlayDetail(List<CardOverview> hand, List<CardOverview> ground, CardOverview weapon,
                              HeroOverview hero, HeroPowerOverview heroPower, String eventLog, int mana, int deckCards) {
        ((PlayPanel) panels.get(PanelType.PLAY)).setDetails(hand, ground
                , weapon, hero, heroPower, eventLog, mana, deckCards);
        if (now != PanelType.PLAY) {
            now = PanelType.PLAY;
            updateFrame();
        }
    }

    public class LoginPanelAction {
        public void changeMode(LoginPanel loginPanel) {
            if (loginPanel.getMode() == LoginPanel.Mode.SIGN_IN) {
                loginPanel.setMode(LoginPanel.Mode.SIGN_UP);
            } else {
                loginPanel.setMode(LoginPanel.Mode.SIGN_IN);
            }
            loginPanel.reset();
        }

        public void login(LoginPanel loginPanel, String username, String pass, String pass2) {
            if ("Enter username".equals(username) || "".equals(username))
                return;
            if (loginPanel.getMode() == LoginPanel.Mode.SIGN_UP && !pass.equals(pass2)) {
                loginPanel.setMessage("password not same");
                return;
            }
            if ("Enter password".equals(pass))
                return;
            sendLoginRequest(username, pass, loginPanel.getMode().getValue());
        }

        public void exit() {
            Client.this.exit();
        }
    }

    public class MainMenuAction {
        public void exit() {
            connector.save(new ButtonLog(username, "exit", MAIN_MENU.toString()));
            Client.this.logout();
            Client.this.exit();
        }

        public void logout() {
            connector.save(new ButtonLog(username, "logout", MAIN_MENU.toString()));
            Client.this.logout();
            ((CollectionPanel) panels.get(PanelType.COLLECTION)).reset();
            now = PanelType.LOGIN;
            updateFrame();
            history.clear();
        }

        public void deleteAccount() {
            connector.save(new ButtonLog(username, "deleteAccount", MAIN_MENU.toString()));
            Client.this.deleteAccount();
            ((CollectionPanel) panels.get(PanelType.COLLECTION)).reset();
            now = PanelType.LOGIN;
            updateFrame();
            history.clear();
        }

        public void shop() {
            connector.save(new ButtonLog(username, "shop", MAIN_MENU.toString()));
            sendShopRequest();
        }

        public void status() {
            connector.save(new ButtonLog(username, "status", MAIN_MENU.toString()));
            sendStatusRequest();
        }

        public void collection() {
            connector.save(new ButtonLog(username, "collection", MAIN_MENU.toString()));
            sendFirstCollectionRequest();
        }

        public void play() {
            connector.save(new ButtonLog(username, "play", MAIN_MENU.toString()));
            sendStartPlayRequest();
        }

        public void update() {
            ((MainMenuPanel) panels.get(PanelType.MAIN_MENU)).setMessage("welcome " + username);
        }
    }

    public class ShopAction {
        public void sell(String cardName) {
            connector.save(new ButtonLog(username, "sell:" + cardName, SHOP.toString()));
            Request request = new SellCard(cardName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
        }

        public void buy(String cardName) {
            connector.save(new ButtonLog(username, "buy:" + cardName, SHOP.toString()));
            Request request = new BuyCard(cardName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
        }

        public void exit() {
            connector.save(new ButtonLog(username, "exit", SHOP.toString()));
            Client.this.logout();
            Client.this.exit();
        }

        public void back() {
            connector.save(new ButtonLog(username, "back", SHOP.toString()));
            Client.this.back();
        }

        public void backMainMenu() {
            connector.save(new ButtonLog(username, "backMainMenu", SHOP.toString()));
            Client.this.backMainMenu();
        }

        public void update() {
            sendShopRequest();
        }
    }

    public class StatusAction {
        public void exit() {
            connector.save(new ButtonLog(username, "exit", STATUS.toString()));
            Client.this.logout();
            Client.this.exit();
        }

        public void back() {
            connector.save(new ButtonLog(username, "back", STATUS.toString()));
            Client.this.back();
        }

        public void backMainMenu() {
            connector.save(new ButtonLog(username, "backMainMenu", STATUS.toString()));
            Client.this.backMainMenu();
        }

        public void update() {
            sendStatusRequest();
        }
    }

    public class CollectionAction {
        private String name = null, classOfCard = null, deckName = null;
        private int mana = 0, lockMode = 0;

        public void exit() {
            connector.save(new ButtonLog(username, "exit", COLLECTION.toString()));
            Client.this.logout();
            Client.this.exit();
        }

        public void back() {
            connector.save(new ButtonLog(username, "back", COLLECTION.toString()));
            Client.this.back();
        }

        public void backMainMenu() {
            connector.save(new ButtonLog(username, "backMainMenu", COLLECTION.toString()));
            Client.this.backMainMenu();
        }

        public void mana(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem().equals("all")) mana = 0;
                else mana = Integer.parseInt((String) e.getItem());
                sendRequest();
                connector.save(new ButtonLog(username, "mana to:" + mana, COLLECTION.toString()));
            }
        }

        public void lockMode(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                if (item.equals("all cards")) lockMode = 0;
                if (item.equals("locked cards")) lockMode = 1;
                if (item.equals("unlocked cards")) lockMode = 2;
                sendRequest();
                connector.save(new ButtonLog(username, "lock made to:" + lockMode, COLLECTION.toString()));
            }
        }

        public void search(DocumentEvent e) {
            try {
                name = e.getDocument().getText(0, e.getDocument().getLength());
                connector.save(new ButtonLog(username, "name to:" + name, COLLECTION.toString()));
            } catch (BadLocationException ignore) {
            }
            sendRequest();
        }

        public void classOfCard(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                if (item.equals("All classes")) classOfCard = null;
                else classOfCard = (String) item;
                sendRequest();
                connector.save(new ButtonLog(username, "class of card to:" + classOfCard
                        , COLLECTION.toString()));
            }
        }

        public void selectDeck(String deckName) {
            if (deckName.equals(this.deckName)) this.deckName = null;
            else this.deckName = deckName;
            connector.save(new ButtonLog(username, "select deck:" + deckName, COLLECTION.toString()));
            sendRequest();
        }

        public void newDeck(String deckName, String heroName) {
            Request request = new NewDeck(deckName, heroName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "new deck:" + deckName + " hero:" + heroName
                    , COLLECTION.toString()));
        }

        public void deleteDeck(String deckName) {
            Request request = new DeleteDeck(deckName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "delete deck:" + deckName, COLLECTION.toString()));
        }

        public void changeDeckName(String oldDeckName, String newDeckName) {
            Request request = new ChangeDeckName(oldDeckName, newDeckName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "change deck name:" + oldDeckName + " new:" + newDeckName,
                    COLLECTION.toString()));
        }

        public void changeHeroDeck(String deckName, String heroName) {
            Request request = new ChangeHeroDeck(deckName, heroName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "change hero deck:" + deckName + " hero:" + heroName
                    , COLLECTION.toString()));
        }

        public void addCardToDeck(String cardName) {
            Request request = new AddCardToDeck(cardName, deckName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "add card to deck:" + deckName + "card:" + cardName
                    , COLLECTION.toString()));
        }

        public void removeCardFromDeck(String cardName) {
            Request request = new RemoveCardFromDeck(cardName, deckName);
            requestSender.sendRequest(request);
            connector.save(new RequestLog(request, Client.this.username));
            connector.save(new ButtonLog(username, "remove card to deck:" + deckName + "card:" + cardName
                    , COLLECTION.toString()));
        }

        public void reset() {
            name = null;
            classOfCard = null;
            deckName = null;
            mana = 0;
            lockMode = 0;
        }

        public void sendRequest() {
            sendCollectionRequest(name, classOfCard, mana, lockMode, deckName);
        }

        public void update() {
            if (!((CollectionPanel) panels.get(PanelType.COLLECTION)).hasFirst())
                Client.this.sendFirstCollectionRequest();
            sendRequest();
        }
    }

    public class PassiveAction {
        public void exit() {
            connector.save(new ButtonLog(username, "exit", PASSIVE.toString()));
            Client.this.logout();
            Client.this.exit();
        }

        public void back() {
            connector.save(new ButtonLog(username, "back", PASSIVE.toString()));
            Client.this.back();
        }

        public void backMainMenu() {
            connector.save(new ButtonLog(username, "backMainMenu", PASSIVE.toString()));
            Client.this.backMainMenu();
        }

        public void selectPassive(String passiveName) {
            Request request = new SelectPassive(passiveName);
            requestSender.sendRequest(request);
            connector.save(new ButtonLog(username, "passive:" + passiveName, PASSIVE.toString()));
            connector.save(new RequestLog(request, Client.this.username));
        }
    }

    public class PlayAction {
        public void exit() {
            Request request = new ExitGame();
            requestSender.sendRequest(request);
            connector.save(new ButtonLog(username, "exit", PLAY.toString()));
            connector.save(new RequestLog(request, Client.this.username));
        }

        public void endTurn() {
            Request request = new EndTurn();
            requestSender.sendRequest(request);
            connector.save(new ButtonLog(username, "end turn", PLAY.toString()));
            connector.save(new RequestLog(request, Client.this.username));
        }

        public void playCard(String cardName) {
            Request request = new PlayCard(cardName);
            requestSender.sendRequest(request);
            connector.save(new ButtonLog(username, "playCard:" + cardName, PLAY.toString()));
            connector.save(new RequestLog(request, Client.this.username));
        }

    }
}