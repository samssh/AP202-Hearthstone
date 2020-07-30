package ir.sam.hearthstone.requests;

public class ExitGame extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.exitGame();
    }
}