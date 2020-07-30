package ir.sam.hearthstone.requests;

public class EndTurn extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.endTurn();
    }
}