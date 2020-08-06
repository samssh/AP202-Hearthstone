package ir.sam.hearthstone.server.model.response;

public class Logout extends Response {

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.doLogout();
    }
}
