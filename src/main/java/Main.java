import client.Client;
import configs.ConfigFactory;
import server.Server;
import util.ImageLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ConfigFactory.setArgs(args);
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            File file = new File("./src/main/resources/fonts");
            File[] files = file.listFiles();
            for (File value : Objects.requireNonNull(files)) {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, value));
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        Client client = Client.getInstance();
        Server server = Server.getInstance();
    }
}
