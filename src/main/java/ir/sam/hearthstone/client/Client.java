package ir.sam.hearthstone.client;

import ir.sam.hearthstone.Transmitters.RequestSender;
import ir.sam.hearthstone.client.Actions.*;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.*;
import ir.sam.hearthstone.requests.*;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.util.Loop;
import ir.sam.hearthstone.util.Updatable;
import ir.sam.hearthstone.view.MyFrame;
import ir.sam.hearthstone.view.PanelType;
import ir.sam.hearthstone.view.model.*;
import ir.sam.hearthstone.view.panel.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.*;

import static ir.sam.hearthstone.view.PanelType.*;

public class Client {
    private final JFrame frame;
    @Getter
    private final Map<PanelType, JPanel> panels;
    @Getter
    private final Stack<PanelType> history;
    @Getter
    @Setter
    private PanelType now;
    private final Connector connector;
    private final List<Response> tempResponseList, responseList;
    private final Loop executor;
    @Getter
    private String username;
    @Getter
    private final RequestSender requestSender;

    public Client(RequestSender requestSender) {
        SwingUtilities.invokeLater(() -> {
        });
        this.requestSender = requestSender;
        this.frame = new MyFrame();
        panels = new EnumMap<>(PanelType.class);
        history = new Stack<>();
        panels.put(LOGIN, new LoginPanel(new LoginPanelAction(this)));
        now = LOGIN;
        updateFrame();
        connector = new Connector(
                ConfigFactory.getInstance().getConfigFile("CLIENT_HIBERNATE_CONFIG"));
        panels.put(MAIN_MENU, new MainMenuPanel(new MainMenuAction(connector, this)));
        panels.put(SHOP, new ShopPanel(new ShopAction(connector, this)));
        panels.put(STATUS, new StatusPanel(new StatusAction(connector, this)));
        panels.put(COLLECTION, new CollectionPanel(new CollectionAction(connector, this)));
        panels.put(PASSIVE, new PassivePanel(new PassiveAction(connector, this)));
        panels.put(PLAY_MODE, new PlayModePanel(new PlayModeAction(connector, this)));
        panels.put(PLAY, new PlayPanel(new PlayAction(connector, this)));
        tempResponseList = new LinkedList<>();
        responseList = new LinkedList<>();
        executor = new Loop(60, this::executeAnswers);
        executor.start();
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

    public void updateFrame() {
        frame.setContentPane(panels.get(now));
        history.push(now);
    }

    public void exit() {
        requestSender.sendRequest(new ShutdownRequest());
        this.shutdown();
        System.exit(0);
    }

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

    public void logout() {
        Request request = new LogoutRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void deleteAccount() {
        Request request = new DeleteAccount();
        requestSender.sendRequest(request);
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
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendShopRequest() {
        Request request = new ShopRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendStatusRequest() {
        Request request = new StatusRequest();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendCollectionRequest(String name, String classOfCard, int mana, int lockMode) {
        Request request = new CollectionFilter(name, classOfCard, mana, lockMode);
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void sendAllCollectionDetailsRequest(String name, String classOfCard, int mana, int lockMode) {
        Request request = new AllCollectionDetails(name, classOfCard, mana, lockMode);
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

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

    public void putShopEvent(String cardName, String type, int coins) {
        ShopPanel shopPanel = (ShopPanel) panels.get(SHOP);
        shopPanel.putShopEvent(cardName, type, coins);
    }


    public void setStatusDetails(List<BigDeckOverview> bigDeckOverviews) {
        StatusPanel statusPanel = (StatusPanel) panels.get(STATUS);
        statusPanel.setDeckBoxList(bigDeckOverviews);
        if (now != STATUS) {
            now = STATUS;
            updateFrame();
        }
    }

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

    public void putCollectionDeckEvent(String type, String deckName, SmallDeckOverview newDeck) {
        ((CollectionPanel) panels.get(COLLECTION)).putDeckEvent(type, deckName, newDeck);
    }

    public void putCollectionCardEvent(String type, String cardName, boolean canAddDeck
            , boolean canChangeHero) {
        ((CollectionPanel) panels.get(COLLECTION)).putCardEvent(type, cardName, canAddDeck, canChangeHero);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

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

    public void sendStartPlayRequest() {
        Request request = new StartPlaying();
        requestSender.sendRequest(request);
        connector.save(new RequestLog(request, username));
    }

    public void setPassives(List<PassiveOverview> passives, List<SmallDeckOverview> decks
            , List<CardOverview> cards, String message, boolean showButton) {
        ((PassivePanel) panels.get(PASSIVE)).setDetails(passives, decks, cards, message,showButton);
        if (now != PASSIVE) {
            now = PASSIVE;
            updateFrame();
        }
    }
    public void changeCardOnPassive(CardOverview cardOverview,int index){
        ((PassivePanel) panels.get(PASSIVE)).changeCard(cardOverview,index);
    }

    public void setPlayDetail(List<PlayDetails.Event> events, String eventLog
            , int[] mana) {
        ((PlayPanel) panels.get(PLAY)).setDetails(events,eventLog,mana);
        if (now != PLAY) {
            now = PLAY;
            updateFrame();
        }
    }

}