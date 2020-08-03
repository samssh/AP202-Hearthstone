package ir.sam.hearthstone.client.actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.server.model.log.ButtonLog;
import ir.sam.hearthstone.server.model.log.RequestLog;
import ir.sam.hearthstone.requests.*;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.awt.event.ItemEvent;

import static ir.sam.hearthstone.client.view.PanelType.COLLECTION;

public class CollectionAction {
    private String name = null, classOfCard = null, deckName = null;
    private int mana = 0, lockMode = 0;
    private final Connector connector;
    private final Client client;

    public CollectionAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void exit() {
        connector.save(new ButtonLog(client.getUsername(), "exit", COLLECTION.toString()));
        client.logout();
        client.exit();
    }

    public void back() {
        connector.save(new ButtonLog(client.getUsername(), "back", COLLECTION.toString()));
        client.back();
    }

    public void backMainMenu() {
        connector.save(new ButtonLog(client.getUsername(), "backMainMenu", COLLECTION.toString()));
        client.backMainMenu();
    }

    public void mana(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getItem().equals("all")) mana = 0;
            else mana = Integer.parseInt((String) e.getItem());
            sendFilterRequest();
            connector.save(new ButtonLog(client.getUsername(), "mana to:" + mana, COLLECTION.toString()));
        }
    }

    public void lockMode(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();
            if (item.equals("all cards")) lockMode = 0;
            if (item.equals("locked cards")) lockMode = 1;
            if (item.equals("unlocked cards")) lockMode = 2;
            sendFilterRequest();
            connector.save(new ButtonLog(client.getUsername(), "lock made to:" + lockMode, COLLECTION.toString()));
        }
    }

    public void search(DocumentEvent e) {
        try {
            name = e.getDocument().getText(0, e.getDocument().getLength());
            connector.save(new ButtonLog(client.getUsername(), "name to:" + name, COLLECTION.toString()));
        } catch (BadLocationException ignore) {
        }
        sendFilterRequest();
    }

    public void classOfCard(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();
            if (item.equals("All classes")) classOfCard = null;
            else classOfCard = (String) item;
            sendFilterRequest();
            connector.save(new ButtonLog(client.getUsername(), "class of card to:" + classOfCard
                    , COLLECTION.toString()));
        }
    }

    public void selectDeck(String deckName) {
        if (deckName.equals(this.deckName)) this.deckName = null;
        else this.deckName = deckName;
        client.addRequest(new SelectDeck(deckName));
        connector.save(new ButtonLog(client.getUsername(), "select deck:" + deckName, COLLECTION.toString()));
    }

    public void newDeck(String deckName, String heroName) {
        client.addRequest(new NewDeck(deckName, heroName));
        connector.save(new ButtonLog(client.getUsername(), "new deck:" + deckName + " hero:" + heroName
                , COLLECTION.toString()));
    }

    public void deleteDeck(String deckName) {
        client.addRequest(new DeleteDeck(deckName));
        connector.save(new ButtonLog(client.getUsername(), "delete deck:" + deckName, COLLECTION.toString()));
    }

    public void changeDeckName(String oldDeckName, String newDeckName) {
        client.addRequest(new ChangeDeckName(oldDeckName, newDeckName));
        connector.save(new ButtonLog(client.getUsername(), "change deck name:" + oldDeckName + " new:" + newDeckName,
                COLLECTION.toString()));
    }

    public void changeHeroDeck(String deckName, String heroName) {
        client.addRequest(new ChangeHeroDeck(deckName, heroName));
        connector.save(new ButtonLog(client.getUsername(), "change hero deck:" + deckName + " hero:" + heroName
                , COLLECTION.toString()));
    }

    public void addCardToDeck(String cardName) {
        client.addRequest(new AddCardToDeck(cardName, deckName));
        connector.save(new ButtonLog(client.getUsername(), "add card to deck:" + deckName + "card:" + cardName
                , COLLECTION.toString()));
    }

    public void removeCardFromDeck(String cardName) {
        client.addRequest(new RemoveCardFromDeck(cardName, deckName));
        connector.save(new ButtonLog(client.getUsername(), "remove card to deck:" + deckName + "card:" + cardName
                , COLLECTION.toString()));
    }

    public void reset() {
        name = null;
        classOfCard = null;
        deckName = null;
        mana = 0;
        lockMode = 0;
    }

    public void sendFilterRequest() {
        client.sendCollectionRequest(name, classOfCard, mana, lockMode);
    }

    public void sendAllCollectionDetailsRequest() {
        client.sendAllCollectionDetailsRequest(name, classOfCard, mana, lockMode);
    }

    public void update() {
        sendAllCollectionDetailsRequest();
    }
}