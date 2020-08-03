package ir.sam.hearthstone.client.model.response;

public abstract class Response {
    public abstract void execute(ResponseExecutor responseExecutor);
}