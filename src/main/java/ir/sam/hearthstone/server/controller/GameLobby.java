package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilderGenerator;
import ir.sam.hearthstone.server.controller.logic.game.online.StandardOnlineGameBuilder;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

import java.util.HashMap;
import java.util.Map;

public class GameLobby {
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Map<String,WaitingRoom> waitingRoomMap;

    public GameLobby(Connector connector, ModelLoader modelLoader) {
        this.connector = connector;
        this.modelLoader = modelLoader;
        waitingRoomMap = new HashMap<>();
        waitingRoomMap.put("online",new WaitingRoom(StandardOnlineGameBuilder::new));
    }

    private class WaitingRoom {
        private final OnlineGameBuilderGenerator generator;
        private OnlineGameBuilder gameBuilder;

        private WaitingRoom(OnlineGameBuilderGenerator generator) {
            this.generator = generator;
        }
    }
}
