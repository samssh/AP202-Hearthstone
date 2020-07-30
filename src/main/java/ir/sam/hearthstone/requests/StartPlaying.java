package ir.sam.hearthstone.requests;

public class StartPlaying extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.startPlay();
    }
}