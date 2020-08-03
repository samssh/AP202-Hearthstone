package ir.sam.hearthstone.client.model.requests;

public class SelectHero extends Request {
    private final int side;

    public SelectHero(int side) {
        this.side = side;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectHero(side);
    }
}
