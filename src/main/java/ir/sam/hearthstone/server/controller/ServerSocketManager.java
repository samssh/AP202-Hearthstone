package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.network.ResponseSender;
import ir.sam.hearthstone.server.controller.network.SocketResponseSender;
import ir.sam.hearthstone.server.resource_loader.Config;
import ir.sam.hearthstone.server.resource_loader.ConfigFactory;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ServerSocketManager {
    private final ServerSocket serverSocket;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final List<ClientHandler> clientHandlers;
    private final GameLobby gameLobby;
    private volatile boolean running;

    public ServerSocketManager() throws IOException {
        Config config = ConfigFactory.getInstance().getConfig("SERVER_CONFIG");
        int port = config.getProperty(Integer.class, "PORT");
        serverSocket = new ServerSocket(port);
        connector = new Connector(ConfigFactory.getInstance().getConfigFile("SERVER_HIBERNATE_CONFIG")
                , System.getenv("HearthStone password"));
        modelLoader = new ModelLoader(connector);
        running = true;
        clientHandlers = Collections.synchronizedList(new ArrayList<>());
        gameLobby = new GameLobby(connector,modelLoader);
    }

    public void start() {
        new Thread(this::getOrders).start();
        new Thread(this::accept).start();
    }

    private void accept() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ResponseSender responseSender = new SocketResponseSender(this, socket);
                ClientHandler clientHandler = new ClientHandler(responseSender, connector, modelLoader, gameLobby);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            } catch (IOException ignore) {
            }
        }
    }

    public void removeClientHandler(SocketResponseSender socketResponseSender) {
        clientHandlers.removeIf(clientHandler -> clientHandler.getResponseSender().equals(socketResponseSender));
    }

    private void getOrders() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("type exit to shutdown server. make sure no client connected");
            if ("exit".equals(scanner.nextLine())) {
                System.out.println("try exit");
                for (ClientHandler clientHandler : clientHandlers)
                    clientHandler.shutdown();
                running = false;
                connector.close();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
