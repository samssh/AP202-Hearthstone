package ir.sam.hearthstone.requests;

public class StatusRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendStatus();
    }
}