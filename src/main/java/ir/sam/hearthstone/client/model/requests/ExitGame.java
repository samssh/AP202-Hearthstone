package ir.sam.hearthstone.client.model.requests;

public class ExitGame extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.exitGame();
    }
}