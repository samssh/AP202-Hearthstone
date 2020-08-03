package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;

public class LoginRequest extends Request {
    @Getter
    private final String userName, password;
    @Getter
    private final int mode;

    public LoginRequest(String userName, String password, int mode) {
        this.userName = userName;
        this.password = password;
        this.mode = mode;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.login(userName, password, mode);
    }
}