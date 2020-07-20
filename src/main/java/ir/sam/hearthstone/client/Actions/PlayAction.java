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

    public void selectCardInHand(int side, String index) {
        int index1 = Integer.parseInt(index);
        Request request = new SelectCardInHand(side, index1);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername()
                , "selectCardInHand,side:" + side + " index:" + index, PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void selectMinion(int side, String index) {
        String[] indexes = index.split(",");
        int index1 = Integer.parseInt(indexes[0]);
        int emptyIndex = Integer.parseInt(indexes[1]) + index1;
        Request request = new SelectMinion(side, index1, emptyIndex);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername()
                , "selectMinion,side:" + side + " index:" + index, PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }

    public void selectHeroPower(int side) {
        Request request = new SelectHeroPower(side);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername()
                , "selectHeroPower,side:" + side, PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));

    }

    public void selectHero(int side) {
        Request request = new SelectHero(side);
        client.getRequestSender().sendRequest(request);
        connector.save(new ButtonLog(client.getUsername()
                , "selectHero,side:" + side, PLAY.toString()));
        connector.save(new RequestLog(request, client.getUsername()));
    }
}