package ir.sam.hearthstone.requests;

import lombok.Getter;
import ir.sam.hearthstone.server.Server;

public class ChangeDeckName extends Request {
    @Getter
    private final String oldDeckName, newDeckName;

    public ChangeDeckName(String oldDeckName, String newDeckName) {
        this.oldDeckName = oldDeckName;
        this.newDeckName = newDeckName;
    }

    @Override
    public void execute(Server server) {
        server.changeDeckName(oldDeckName, newDeckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setChangeDeckName(this);
    }
}

