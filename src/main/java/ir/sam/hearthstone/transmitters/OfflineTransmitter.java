package ir.sam.hearthstone.transmitters;

import ir.sam.hearthstone.requests.Request;
import ir.sam.hearthstone.response.Response;

import java.util.LinkedList;
import java.util.Queue;

public class OfflineTransmitter implements RequestSender, ResponseSender {
    private final Queue<Request> requests;
    private final Queue<Response[]> responsesList;

    public OfflineTransmitter() {
        requests = new LinkedList<>();
        responsesList = new LinkedList<>();
    }

    public Request getRequest() {
        synchronized (requests) {
            while (requests.size() == 0) {
                try {
                    requests.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return requests.remove();
        }
    }

    @Override
    public void sendResponse(Response... responses) {
        synchronized (responsesList) {
            responsesList.add(responses);
            responsesList.notifyAll();
        }
    }

    @Override
    public Response[] sendRequest(Request request) {
        synchronized (requests) {
            requests.add(request);
            requests.notifyAll();
        }
        synchronized (responsesList) {
            while (responsesList.size() == 0) {
                try {
                    responsesList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return responsesList.remove();
        }
    }
}
