package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.util.Visitable;

public abstract class Request implements Visitable<RequestLogInfoVisitor> {
    public abstract void execute(Server server);
}
