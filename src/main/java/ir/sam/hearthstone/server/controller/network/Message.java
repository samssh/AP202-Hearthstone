package ir.sam.hearthstone.server.controller.network;


import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.response.Response;
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

    public Message(String token, Response[] responses) {
        this.token = token;
        this.responses = responses;
    }

    public Message() { // response type

    }
}
