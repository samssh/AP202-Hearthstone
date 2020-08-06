package ir.sam.hearthstone.server.model.response;

public class ShutDown extends Response {

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.doShutDown();
    }
}
