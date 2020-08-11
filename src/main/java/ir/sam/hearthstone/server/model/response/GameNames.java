package ir.sam.hearthstone.server.model.response;

import java.util.List;

public class GameNames extends Response {
    private final List<String> names;

    public GameNames(List<String> names) {
        this.names = names;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.gameNames(names);
    }
}
