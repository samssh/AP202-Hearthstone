import client.Client;
import util.ConfigFactory;
import server.Server;
import util.ImageLoader;
import util.ResourceLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ConfigFactory.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        ImageLoader imageLoader = ImageLoader.getInstance();
        Server server = Server.getInstance();
        Client client = Client.getInstance();
    }
}
