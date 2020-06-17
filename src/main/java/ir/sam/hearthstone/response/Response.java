package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.util.Visitable;

public abstract class Response implements Visitable<ResponseLogInfoVisitor> {
    public abstract void execute(Client client);
}