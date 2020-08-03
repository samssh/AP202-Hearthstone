package ir.sam.hearthstone.server.model.requests;

public class StartPlaying extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.startPlay();
    }
}