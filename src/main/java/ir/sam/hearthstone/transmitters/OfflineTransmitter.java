package ir.sam.hearthstone.transmitters;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.requests.Request;
import ir.sam.hearthstone.response.Response;
import ir.sam.hearthstone.server.Server;
import lombok.Setter;

public class OfflineTransmitter implements RequestSender, ResponseSender {
    @Setter
    private Server server;
    @Setter
    private Client client;

    public OfflineTransmitter() {
    }

    @Override
    public void sendRequest(Request request) {
        server.addRequest(request);
    }

    @Override
    public void sendResponse(Response response) {
        client.addResponse(response);
    }
}
