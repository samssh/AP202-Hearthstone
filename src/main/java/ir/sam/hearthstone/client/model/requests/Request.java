package ir.sam.hearthstone.client.model.requests;

public abstract class Request {
    public abstract void execute(RequestExecutor requestExecutor);
}
