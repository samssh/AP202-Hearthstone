package ir.sam.hearthstone.client.model.requests;

import lombok.Getter;

public class ChangeDeckName extends Request {
    @Getter
    private final String oldDeckName, newDeckName;

    public ChangeDeckName(String oldDeckName, String newDeckName) {
        this.oldDeckName = oldDeckName;
        this.newDeckName = newDeckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.changeDeckName(oldDeckName, newDeckName);
    }
}

