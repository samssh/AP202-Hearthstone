package ir.sam.hearthstone.requests;

public class ShutdownRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.shutdown();
    }
}
