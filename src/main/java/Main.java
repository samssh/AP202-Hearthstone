import client.Client;
import configs.ConfigFactory;
//import controller.MenuController;
//import controller.MenuMaker;
import hibernate.Connector;
import model.ModelLoader;
import server.Server;
import view.MyFrame;
import view.panel.LoginPanel;

import java.awt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        ConfigFactory.getInstance("DEFAULT");
        Client client = Client.getInstance();
        Server server = Server.getInstance();

//        System.exit(0);
    }
}
