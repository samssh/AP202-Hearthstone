package ir.sam.hearthstone.server.model.requests;

public class StatusRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendStatus();
    }
}