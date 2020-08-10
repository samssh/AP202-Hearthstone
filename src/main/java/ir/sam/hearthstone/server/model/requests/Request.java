package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;

public abstract class Request {
    public abstract void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException;
}
