package ir.sam.hearthstone.server;


import ir.sam.hearthstone.server.controller.ServerSocketManager;
import ir.sam.hearthstone.server.resource_loader.ConfigFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ConfigFactory.setArgs(args);
        new ServerSocketManager().start();
    }
}
