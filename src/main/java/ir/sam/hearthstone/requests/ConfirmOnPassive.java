package ir.sam.hearthstone.requests;

public class ConfirmOnPassive extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.confirm();
    }
}
