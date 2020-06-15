package requests;

import lombok.Getter;
import server.Server;

public class SelectPassive extends Request {
    @Getter
    private final String passiveName;

    public SelectPassive(String passiveName) {
        this.passiveName = passiveName;
    }

    @Override
    public void execute(Server server) {
        server.selectPassive(passiveName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setSelectPassive(this);
    }
}