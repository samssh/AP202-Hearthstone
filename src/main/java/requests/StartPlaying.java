package requests;

import server.Server;

public class StartPlaying extends Request {
    @Override
    public void execute() {
        Server.getInstance().startPlay();
    }
}