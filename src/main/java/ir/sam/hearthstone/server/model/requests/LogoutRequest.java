package ir.sam.hearthstone.server.model.requests;

public class LogoutRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.logout();
    }
}