package ir.sam.hearthstone.client;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.resource_manager.ResourceLoader;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        new Client().start();
    }
}
