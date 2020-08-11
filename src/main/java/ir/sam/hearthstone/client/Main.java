package ir.sam.hearthstone.client;

import ir.sam.hearthstone.client.controller.Client;
import ir.sam.hearthstone.client.resource_manager.ResourceLoader;

import java.io.File;
import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        File file = new File("E:\\code\\test\\ref_test.jar");
        System.out.println(file.toURI().toURL());
//        ResourceLoader.setArgs(args);
//        ResourceLoader.getInstance().checkResources();
//        new Client().start();
    }
}
