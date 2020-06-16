package ir.SAM.hearthstone;

import ir.SAM.hearthstone.Transmitters.OfflineTransmitter;
import ir.SAM.hearthstone.client.Client;
import ir.SAM.hearthstone.server.Server;
import ir.SAM.hearthstone.resourceManager.ResourceLoader;

public class Main {
    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        OfflineTransmitter offlineTransmitter = new OfflineTransmitter();
        offlineTransmitter.setServer(new Server(offlineTransmitter));
        offlineTransmitter.setClient(new Client(offlineTransmitter));
    }
}
