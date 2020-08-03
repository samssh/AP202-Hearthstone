package ir.sam.hearthstone.server.model.requests;

public class EndTurn extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.endTurn();
    }
}