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
    public void execute() {
        Server.getInstance().selectPassive(passiveName);
    }
}