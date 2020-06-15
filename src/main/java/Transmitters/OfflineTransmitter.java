package Transmitters;

import client.Client;
import lombok.Setter;
import requests.Request;
import response.Response;
import server.Server;

public class OfflineTransmitter implements RequestSender, ResponseSender {
    @Setter
    private Server server;
    @Setter
    private Client client;

    public OfflineTransmitter(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

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
