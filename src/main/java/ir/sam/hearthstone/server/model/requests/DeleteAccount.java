package ir.sam.hearthstone.server.model.requests;

public class DeleteAccount extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.deleteAccount();
    }
}