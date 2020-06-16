package ir.SAM.hearthstone.Transmitters;

import ir.SAM.hearthstone.client.Client;
import lombok.Setter;
import ir.SAM.hearthstone.requests.Request;
import ir.SAM.hearthstone.response.Response;
import ir.SAM.hearthstone.server.Server;

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
