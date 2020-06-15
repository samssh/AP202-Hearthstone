package requests;

import server.Server;

public class DeleteAccount extends Request {
    @Override
    public void execute(Server server) {
        server.deleteAccount();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setDeleteAccount(this);
    }
}