package ir.sam.hearthstone.client.model.requests;

public class CheckForOpponente extends Request{
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.checkForOpponent();
    }
}
