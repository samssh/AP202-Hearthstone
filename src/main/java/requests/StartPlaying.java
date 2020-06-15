package requests;

import server.Server;

public class StartPlaying extends Request {
    @Override
    public void execute(Server server) {
        server.startPlay();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setStartPlaying(this);
    }
}