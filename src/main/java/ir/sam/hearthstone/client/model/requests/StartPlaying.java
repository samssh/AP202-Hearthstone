package ir.sam.hearthstone.client.model.requests;

public class StartPlaying extends Request {
    private final String mode;

    public StartPlaying(String mode) {
        this.mode = mode;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.startPlay(mode);
    }
}