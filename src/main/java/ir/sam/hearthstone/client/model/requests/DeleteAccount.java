package ir.sam.hearthstone.client.model.requests;

public class DeleteAccount extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.deleteAccount();
    }
}