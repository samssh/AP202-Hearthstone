package ir.sam.hearthstone.server.model.response;

public abstract class Response {
    public abstract void execute(ResponseExecutor responseExecutor);
}