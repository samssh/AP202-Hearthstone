package ir.sam.hearthstone.transmitters;

import ir.sam.hearthstone.requests.Request;
import ir.sam.hearthstone.response.Response;

public interface ResponseSender {
    Request getRequest();

    void sendResponse(Response... responses);
}
