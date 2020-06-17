package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;

public class ShopRequest extends Request {
    @Override
    public void execute(Server server) {
        server.sendShop();
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setShopRequest(this);
    }
}