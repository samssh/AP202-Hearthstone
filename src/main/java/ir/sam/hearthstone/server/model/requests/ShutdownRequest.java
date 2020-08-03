package ir.sam.hearthstone.server.model.requests;

public class ShutdownRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.shutdown();
    }
}
