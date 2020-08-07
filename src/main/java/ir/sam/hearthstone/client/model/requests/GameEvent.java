package ir.sam.hearthstone.client.model.requests;

public class GameEvent extends Request{

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendGameEvents();
    }
}
