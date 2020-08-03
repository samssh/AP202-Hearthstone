package ir.sam.hearthstone.client.controller.action_listener;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.model.log.ButtonLog;
import ir.sam.hearthstone.client.model.requests.SelectPlayMode;
import ir.sam.hearthstone.client.util.hibernate.Connector;

import static ir.sam.hearthstone.client.view.PanelType.PLAY_MODE;

public class PlayModeAction {
    private final Connector connector;
    private final Client client;

    public PlayModeAction(Connector connector, Client client) {
        this.connector = connector;
        this.client = client;
    }

    public void select(String modeName) {
        connector.save(new ButtonLog(client.getUsername(), "play mode: " + modeName, PLAY_MODE.toString()));
        client.addRequest(new SelectPlayMode(modeName));
    }
}
