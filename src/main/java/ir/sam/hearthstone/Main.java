package ir.sam.hearthstone;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.resource_manager.ResourceLoader;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.transmitters.OfflineTransmitter;

public class Main {
    public static void main(String[] args) {
        ResourceLoader.setArgs(args);
        ResourceLoader.getInstance().checkResources();
        OfflineTransmitter offlineTransmitter = new OfflineTransmitter();
        offlineTransmitter.setServer(new Server(offlineTransmitter));
        offlineTransmitter.setClient(new Client(offlineTransmitter));
    }
}