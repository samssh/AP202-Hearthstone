package ir.sam.hearthstone.server.model.requests;

public class ShopRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendShop();
    }
}