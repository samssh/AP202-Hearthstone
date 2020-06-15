package requests;

import server.Server;
import util.Visitable;

public abstract class Request implements Visitable<RequestLogInfoVisitor>{
    public abstract void execute(Server server);
}
