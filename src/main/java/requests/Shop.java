package requests;

import server.Server;

public class Shop extends Request {
    @Override
    public void execute() {
        Server.getInstance().sendShop();
    }
}