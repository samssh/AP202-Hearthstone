package ir.sam.hearthstone.client.model.requests;

public class ShopRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendShop();
    }
}