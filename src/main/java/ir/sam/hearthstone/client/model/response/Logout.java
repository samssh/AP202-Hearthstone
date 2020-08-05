package ir.sam.hearthstone.client.model.response;

public class Logout extends Response {

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.doLogout();
    }
}
