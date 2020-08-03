package ir.sam.hearthstone.client.model.requests;

public class StartPlaying extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.startPlay();
    }
}