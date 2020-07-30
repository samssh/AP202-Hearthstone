package ir.sam.hearthstone.requests;

public abstract class Request {
    public abstract void execute(RequestExecutor requestExecutor);
}
