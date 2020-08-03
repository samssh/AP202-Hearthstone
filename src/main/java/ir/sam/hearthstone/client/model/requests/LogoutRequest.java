package ir.sam.hearthstone.client.model.requests;

public class LogoutRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.logout();
    }
}