package ir.sam.hearthstone.server.model.requests;

public class CheckForOpponente extends Request{
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.checkForOpponent();
    }
}
