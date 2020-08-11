package ir.sam.hearthstone.client.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GameNames extends Response {
    @Getter
    @Setter
    private List<String> names;

    public GameNames() {
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.showGameNames(names);
    }
}
