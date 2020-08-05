package ir.sam.hearthstone.client.model.response;

public class ShutDown extends Response {

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.doShutDown();
    }
}
