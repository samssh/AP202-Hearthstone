package ir.sam.hearthstone.server.controller.transmitters;

import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.response.Response;

public interface ResponseSender {
    Request getRequest();

    void sendResponse(Response... responses);
}
