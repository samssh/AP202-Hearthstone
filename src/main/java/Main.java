import Transmitters.OfflineTransmitter;
import client.Client;
import server.Server;
import resourceManager.ImageLoader;
import resourceManager.ResourceLoader;

public class Main {
    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        ImageLoader imageLoader = ImageLoader.getInstance();
        OfflineTransmitter offlineTransmitter = new OfflineTransmitter();
        offlineTransmitter.setServer(new Server(offlineTransmitter));
        offlineTransmitter.setClient(new Client(offlineTransmitter));

    }
}
