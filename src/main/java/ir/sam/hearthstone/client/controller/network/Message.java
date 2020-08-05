package ir.sam.hearthstone.client.controller.network;

import ir.sam.hearthstone.client.model.requests.Request;
import ir.sam.hearthstone.client.model.response.Response;
import lombok.Getter;
import lombok.Setter;

public class Message {
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private Request request;
    @Getter
    @Setter
    private Response[] responses;

    public Message(String token, Request request) {
        this.token = token;
        this.request = request;
    }

    public Message() { // response type

    }
}
