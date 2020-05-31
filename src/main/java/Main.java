import client.Client;
import server.Server;
import util.ImageLoader;
import util.ResourceLoader;

public class Main {
    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        ImageLoader imageLoader = ImageLoader.getInstance();
        Server server = Server.getInstance();
        Client client = Client.getInstance();
    }
}
