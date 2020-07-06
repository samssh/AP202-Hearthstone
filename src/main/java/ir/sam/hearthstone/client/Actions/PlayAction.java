package ir.sam.hearthstone.client.Actions;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.hibernate.Connector;
import ir.sam.hearthstone.model.log.*;
import ir.sam.hearthstone.requests.*;

import static ir.sam.hearthstone.view.PanelType.PLAY;

public class PlayAction {
    private final Connector connector;
    private final Client client;

    public PlayAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void exit() {
        Request request = new ExitGame();
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername(), "exit", PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void endTurn() {
        Request request = new EndTurn();
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername(), "end turn", PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void playCard(String cardName) {
        Request request = new PlayCard(cardName);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername(), "playCard:" + cardName, PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }
}