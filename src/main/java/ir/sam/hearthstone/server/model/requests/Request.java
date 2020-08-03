package ir.sam.hearthstone.server.model.requests;

public abstract class Request {
    public abstract void execute(RequestExecutor requestExecutor);
}
