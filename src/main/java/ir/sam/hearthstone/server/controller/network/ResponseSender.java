package ir.sam.hearthstone.server.controller.network;

import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.response.Response;

public interface ResponseSender {
    Request getRequest() throws CliectDisconnectException;

    void sendResponse(Response... responses) throws CliectDisconnectException;

    void close();
}
