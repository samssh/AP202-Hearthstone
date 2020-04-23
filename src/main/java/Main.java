import client.Client;
import configs.ConfigFactory;
import server.Server;
import view.ImageLoader;

public class Main {
    public static void main(String[] args) {
        ConfigFactory.getInstance("DEFAULT");
        ImageLoader imageLoader = ImageLoader.getInstance();
        Client client = Client.getInstance();
        Server server = Server.getInstance();
    }
}
