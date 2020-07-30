package ir.sam.hearthstone.requests;

public class LogoutRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.logout();
    }
}