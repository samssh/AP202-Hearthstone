package ir.sam.hearthstone.client.model.requests;

public class EndTurn extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.endTurn();
    }
}