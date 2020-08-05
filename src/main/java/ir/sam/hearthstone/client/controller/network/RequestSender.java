package ir.sam.hearthstone.client.controller.network;

import ir.sam.hearthstone.client.model.requests.Request;
import ir.sam.hearthstone.client.model.response.Response;

public interface RequestSender {
    Response[] sendRequest(Request request);
}
