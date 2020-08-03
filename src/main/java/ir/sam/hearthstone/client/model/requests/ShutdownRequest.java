package ir.sam.hearthstone.client.model.requests;

public class ShutdownRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.shutdown();
    }
}
