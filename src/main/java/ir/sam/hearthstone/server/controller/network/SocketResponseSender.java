package ir.sam.hearthstone.server.controller.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ir.sam.hearthstone.server.controller.ServerSocketManager;
import ir.sam.hearthstone.server.controller.network.serializer_and_deserializer.Deserializer;
import ir.sam.hearthstone.server.controller.network.serializer_and_deserializer.Serializer;
import ir.sam.hearthstone.server.controller.network.serializer_and_deserializer.SerializerAndDeserializerConstants;
import ir.sam.hearthstone.server.model.client.AbstractCardOverview;
import ir.sam.hearthstone.server.model.client.Overview;
import ir.sam.hearthstone.server.model.client.UnitOverview;
import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.response.LoginResponse;
import ir.sam.hearthstone.server.model.response.Logout;
import ir.sam.hearthstone.server.model.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.Scanner;

public class SocketResponseSender implements ResponseSender {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private final ServerSocketManager serverSocketManager;
    private final Scanner scanner;
    private final PrintStream printStream;
    private final Gson gson;
    private String token;

    public SocketResponseSender(ServerSocketManager serverSocketManager, Socket socket) throws IOException {
        this.serverSocketManager = serverSocketManager;
        scanner = new Scanner(socket.getInputStream());
        printStream = new PrintStream(socket.getOutputStream(), true);
        gson = new GsonBuilder().registerTypeAdapter(Request.class
                , new Deserializer<Request>(SerializerAndDeserializerConstants.REQUESTS_PACKAGE))
                .registerTypeAdapter(Response.class, new Serializer<>())
                .registerTypeAdapter(Overview.class, new Serializer<>())
                .registerTypeAdapter(UnitOverview.class, new Serializer<>())
                .registerTypeAdapter(AbstractCardOverview.class, new Serializer<>())
                .create();
    }

    @Override
    public Request getRequest() {

        Optional<Message> optionalMessage;
        do {
            String encoded = scanner.nextLine();
            String json = new String(decoder.decode(encoded));
            optionalMessage = getRequestMessage(json);
        } while (optionalMessage.isEmpty());
        return optionalMessage.get().getRequest();
    }

    private Optional<Message> getRequestMessage(String json) {
        Message message;
        try {
            message = gson.fromJson(json, Message.class);
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
        if (message != null) {
            if (token != null && !token.equals(message.getToken()))
                return Optional.empty();
            if (message.getRequest() != null) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    @Override
    public void sendResponse(Response... responses) {
        Message message = new Message(token, responses);
        checkToken(message);
        String json = gson.toJson(message);
        String encode = encoder.encodeToString(json.getBytes());
        printStream.println(encode);
    }

    @Override
    public void close() {
        printStream.close();
        scanner.close();
        serverSocketManager.removeClientHandler(this);
    }

    private void checkToken(Message message) {
        if (message.getResponses() != null && message.getResponses().length > 0) {
            if (message.getResponses()[0] instanceof LoginResponse
                    && ((LoginResponse) message.getResponses()[0]).isSuccess()) {
                token = generateNewToken();
                message.setToken(token);
            } else if (message.getResponses()[0] instanceof Logout) {
                token = null;
            }
        }
    }

    public String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
