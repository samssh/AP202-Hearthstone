package ir.sam.hearthstone.transmitters;

import ir.sam.hearthstone.requests.Request;

public interface RequestSender {
    void sendRequest(Request request);
}
