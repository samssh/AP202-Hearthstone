package response;

import client.Client;
import util.ResponseLogInfoVisitor;
import util.Visitable;

public abstract class Response implements Visitable<ResponseLogInfoVisitor> {
    public abstract void execute(Client client);
}