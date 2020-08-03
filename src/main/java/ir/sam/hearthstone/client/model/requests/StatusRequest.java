package ir.sam.hearthstone.client.model.requests;

public class StatusRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendStatus();
    }
}