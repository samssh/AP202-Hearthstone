package ir.SAM.hearthstone.requests;

import ir.SAM.hearthstone.server.Server;
import ir.SAM.hearthstone.util.Visitable;

public abstract class Request implements Visitable<RequestLogInfoVisitor>{
    public abstract void execute(Server server);
}
