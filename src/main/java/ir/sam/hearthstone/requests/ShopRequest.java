package ir.sam.hearthstone.requests;

public class ShopRequest extends Request {
    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendShop();
    }
}