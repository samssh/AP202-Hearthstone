package ir.sam.hearthstone.requests;

public class DeleteAccount extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.deleteAccount();
    }
}