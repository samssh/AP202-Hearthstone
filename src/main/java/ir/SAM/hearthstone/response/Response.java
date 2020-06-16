package ir.SAM.hearthstone.response;

import ir.SAM.hearthstone.client.Client;
import ir.SAM.hearthstone.util.Visitable;

public abstract class Response implements Visitable<ResponseLogInfoVisitor> {
    public abstract void execute(Client client);
}