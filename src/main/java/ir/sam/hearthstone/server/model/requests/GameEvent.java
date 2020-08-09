package ir.sam.hearthstone.server.model.requests;

public class GameEvent extends Request{

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendGameEvents();
    }
}
