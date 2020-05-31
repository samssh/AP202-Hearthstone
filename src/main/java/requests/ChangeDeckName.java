package requests;

import lombok.Getter;
import server.Server;

public class ChangeDeckName extends Request {
    @Getter
    private final String oldDeckName, newDeckName;

    public ChangeDeckName(String oldDeckName, String newDeckName) {
        this.oldDeckName = oldDeckName;
        this.newDeckName = newDeckName;
    }

    @Override
    public void execute() {
        Server.getInstance().changeDeckName(oldDeckName, newDeckName);
    }
}

