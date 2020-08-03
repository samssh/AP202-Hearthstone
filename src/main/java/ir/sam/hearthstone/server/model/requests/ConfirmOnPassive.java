package ir.sam.hearthstone.server.model.requests;

public class ConfirmOnPassive extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.confirm();
    }
}
