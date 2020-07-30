package ir.sam.hearthstone.requests;

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
