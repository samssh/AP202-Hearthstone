package ir.sam.hearthstone.client.controller;

import ir.sam.hearthstone.client.controller.action_listener.*;
import ir.sam.hearthstone.client.controller.network.RequestSender;
import ir.sam.hearthstone.client.model.log.RequestLog;
import ir.sam.hearthstone.client.model.log.ResponseLog;
import ir.sam.hearthstone.client.model.main.BigDeckOverview;
import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.PassiveOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import ir.sam.hearthstone.client.model.requests.*;
import ir.sam.hearthstone.client.model.response.PlayDetails;
import ir.sam.hearthstone.client.model.response.Response;
import ir.sam.hearthstone.client.model.response.ResponseExecutor;
import ir.sam.hearthstone.client.resource_manager.ConfigFactory;
import ir.sam.hearthstone.client.util.Loop;
import ir.sam.hearthstone.client.util.Updatable;
import ir.sam.hearthstone.client.util.hibernate.Connector;
import ir.sam.hearthstone.client.view.MyFrame;
import ir.sam.hearthstone.client.view.PanelType;
import ir.sam.hearthstone.client.view.panel.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.*;

import static ir.sam.hearthstone.client.view.PanelType.*;

public class Client implements ResponseExecutor {
    private final JFrame frame;
    @Getter
    private final Map<PanelType, JPanel> panels;
    @Getter
    private final Stack<PanelType> history;
    @Getter
    @Setter
    private PanelType now;
    private final Connector connector;
    private final List<Request> requestList;
    private final Loop executor;
    @Getter
    private String username;
    @Setter
    private RequestSender requestSender;

    public Client() {
        connector = new Connector(
                ConfigFactory.getInstance().getConfigFile("CLIENT_HIBERNATE_CONFIG"));
        this.frame = new MyFrame();
        panels = new EnumMap<>(PanelType.class);
        history = new Stack<>();
        panels.put(CONNECT, new ConnectPanel(new ConnectAction(this)));
        panels.put(LOGIN, new LoginPanel(new LoginPanelAction(this)));
        panels.put(MAIN_MENU, new MainMenuPanel(new MainMenuAction(connector, this)));
        panels.put(SHOP, new ShopPanel(new ShopAction(connector, this)));
        panels.put(STATUS, new StatusPanel(new StatusAction(connector, this)));
        panels.put(COLLECTION, new CollectionPanel(new CollectionAction(connector, this)));
        panels.put(PASSIVE, new PassivePanel(new PassiveAction(connector, this)));
        panels.put(PLAY_MODE, new PlayModePanel(new PlayModeAction(connector, this)));
        panels.put(PLAY, new PlayPanel(new PlayAction(connector, this)));
        requestList = new LinkedList<>();
        now = CONNECT;
        frame.setContentPane(panels.get(CONNECT));
        executor = new Loop(60, this::executeAnswers);
    }


    public void start() {
        executor.start();
    }

    public void setOnLogin() {
        now = LOGIN;
        updateFrame();
    }

    private void executeAnswers() {
        List<Request> temp;
        synchronized (requestList) {
            temp = new ArrayList<>(requestList);
            requestList.clear();
        }
        for (Request request : temp) {
            connector.save(new RequestLog(request, username));
            Response[] responses = requestSender.sendRequest(request);
            for (Response response : responses) {
                response.execute(this);
                connector.save(new ResponseLog(response, username));
            }
        }
    }

    public void addRequest(Request request) {
        if (request != null) {
            synchronized (requestList) {
                requestList.add(request);
            }
        }
    }

    public void updateFrame() {
        frame.setContentPane(panels.get(now));
        history.push(now);
    }

    public void exit() {
        addRequest(new ShutdownRequest());
    }

    public void sendLogoutRequest() {
        Request request = new LogoutRequest();
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void deleteAccount() {
        Request request = new DeleteAccount();
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void back() {
        history.pop();
        now = history.peek();
        if (panels.get(now) instanceof Updatable)
            ((Updatable) panels.get(now)).update();
        frame.setContentPane(panels.get(now));
    }

    public void backMainMenu() {
        while (history.peek() != MAIN_MENU)
            history.pop();
        now = history.peek();
        frame.setContentPane(panels.get(now));
    }

    public void sendLoginRequest(String username, String pass, int mode) {
        Request request = new LoginRequest(username, pass, mode);
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendShopRequest() {
        Request request = new ShopRequest();
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendStatusRequest() {
        Request request = new StatusRequest();
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendCollectionRequest(String name, String classOfCard, int mana, int lockMode) {
        Request request = new CollectionFilter(name, classOfCard, mana, lockMode);
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendAllCollectionDetailsRequest(String name, String classOfCard, int mana, int lockMode) {
        Request request = new AllCollectionDetails(name, classOfCard, mana, lockMode);
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendStartPlayRequest() {
        Request request = new StartPlaying();
        addRequest(request);
        connector.save(new RequestLog(request, username));
    }

    @Override
    public void doShutDown() {
        connector.close();
        synchronized (requestList) {
            requestList.clear();
            executor.stop();
        }
        System.exit(0);
    }

    @Override
    public void doLogout() {
        ((CollectionPanel) panels.get(COLLECTION)).reset();
        now = PanelType.LOGIN;
        updateFrame();
        history.clear();
    }

    @Override
    public void login(boolean success, String message) {
        LoginPanel panel = (LoginPanel) panels.get(LOGIN);
        if (success) {
            panel.reset();
            now = MAIN_MENU;
            updateFrame();
            username = message;
            ((MainMenuPanel) panels.get(MAIN_MENU)).update();
        } else {
            panel.setMessage(message);
        }
    }

    @Override
    public void setShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coin) {
        if (now != SHOP) {
            now = SHOP;
            updateFrame();
        }
        ShopPanel shopPanel = (ShopPanel) panels.get(SHOP);
        shopPanel.setSell(sell);
        shopPanel.setBuy(buy);
        shopPanel.setCoins(coin);
    }

    @Override
    public void putShopEvent(String cardName, String type, int coins) {
        ShopPanel shopPanel = (ShopPanel) panels.get(SHOP);
        shopPanel.putShopEvent(cardName, type, coins);
    }

    @Override
    public void setStatusDetails(List<BigDeckOverview> bigDeckOverviews) {
        StatusPanel statusPanel = (StatusPanel) panels.get(STATUS);
        statusPanel.setDeckBoxList(bigDeckOverviews);
        if (now != STATUS) {
            now = STATUS;
            updateFrame();
        }
    }

    @Override
    public void setCollectionDetail(List<CardOverview> cards, List<SmallDeckOverview> decks,
                                    List<CardOverview> deckCards, boolean canAddDeck,
                                    boolean canChangeHero, String deckName,
                                    List<String> heroNames, List<String> classOfCardNames) {
        CollectionPanel collectionPanel = (CollectionPanel) panels.get(COLLECTION);
        collectionPanel.setDetails(cards, decks, deckCards, canAddDeck, canChangeHero, deckName, heroNames, classOfCardNames);
        if (now != COLLECTION) {
            now = COLLECTION;
            updateFrame();
        }
    }

    @Override
    public void putCollectionDeckEvent(String type, String deckName, SmallDeckOverview newDeck) {
        ((CollectionPanel) panels.get(COLLECTION)).putDeckEvent(type, deckName, newDeck);
    }

    @Override
    public void putCollectionCardEvent(String type, String cardName, boolean canAddDeck
            , boolean canChangeHero) {
        ((CollectionPanel) panels.get(COLLECTION)).putCardEvent(type, cardName, canAddDeck, canChangeHero);
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void goTo(String panel, String message) {
        try {
            PanelType p = valueOf(panel);
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

    @Override
    public void setPassives(List<PassiveOverview> passives, List<SmallDeckOverview> decks
            , List<CardOverview> cards, String message, boolean showButton) {
        ((PassivePanel) panels.get(PASSIVE)).setDetails(passives, decks, cards, message, showButton);
        if (now != PASSIVE) {
            now = PASSIVE;
            updateFrame();
        }
    }

    @Override
    public void changeCardOnPassive(CardOverview cardOverview, int index) {
        ((PassivePanel) panels.get(PASSIVE)).changeCard(cardOverview, index);
    }

    @Override
    public void setPlayDetail(List<PlayDetails.Event> events, String eventLog
            , int[] mana, long time) {
        ((PlayPanel) panels.get(PLAY)).setDetails(events, eventLog, mana, time);
        if (now != PLAY) {
            now = PLAY;
            updateFrame();
        }
    }
}