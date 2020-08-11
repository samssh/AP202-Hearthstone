package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.game.Side;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilder;
import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilderGenerator;
import ir.sam.hearthstone.server.controller.logic.game.online.StandardOnlineGameBuilder;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLobby {
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final Map<String, WaitingRoom> waitingRoomMap;

    public GameLobby(Connector connector, ModelLoader modelLoader) {
        this.connector = connector;
        this.modelLoader = modelLoader;
        waitingRoomMap = new HashMap<>();
        waitingRoomMap.put("online", new WaitingRoom(StandardOnlineGameBuilder::new));
    }

    private static class WaitingRoom {
        private final OnlineGameBuilderGenerator generator;
        private OnlineGameBuilder gameBuilder;
        private Side sideToSet;

        private WaitingRoom(OnlineGameBuilderGenerator generator) {
            this.generator = generator;
        }
    }

    public synchronized void addGame(String gameName,OnlineGameBuilderGenerator generator){
        waitingRoomMap.put(gameName,new WaitingRoom(generator));
    }

    public synchronized List<String> getGames(){
        List<String> result = new ArrayList<>(waitingRoomMap.keySet());
        result.remove("online");
        return result;
    }

    public synchronized OnlineGameBuilder getGameBuilder(String gameName, ClientHandler clientHandler) {
        if (waitingRoomMap.containsKey(gameName)) {
            WaitingRoom waitingRoom = waitingRoomMap.get(gameName);
            OnlineGameBuilder onlineGameBuilder;
            if (waitingRoom.gameBuilder!=null&& waitingRoom.gameBuilder.isCanceled()){
                waitingRoom.gameBuilder = null;
            }
            if (waitingRoom.gameBuilder == null) {
                waitingRoom.gameBuilder = waitingRoom.generator.generate(modelLoader, connector);
                onlineGameBuilder = waitingRoom.gameBuilder;
                double random = Math.random();
                waitingRoom.sideToSet = random < .5 ? Side.PLAYER_ONE : Side.PLAYER_TWO;
            } else {
                onlineGameBuilder = waitingRoom.gameBuilder;
                waitingRoom.sideToSet = waitingRoom.sideToSet.getOther();
                waitingRoom.gameBuilder = null;
            }
            onlineGameBuilder.setClientHandler(waitingRoom.sideToSet, clientHandler);
            clientHandler.setSide(waitingRoom.sideToSet);
            return onlineGameBuilder;
        } else return null;
    }
}
