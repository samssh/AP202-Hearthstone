package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

public class LoginRequest extends Request {
    @Getter
    @Setter
    private String userName, password;
    @Getter
    @Setter
    private int mode;

    @Override
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.login(userName, password, mode);
    }
}