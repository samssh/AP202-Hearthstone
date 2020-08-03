package ir.sam.hearthstone.transmitters;

import ir.sam.hearthstone.requests.Request;
import ir.sam.hearthstone.response.Response;

public interface RequestSender {
    Response[] sendRequest(Request request);
}
