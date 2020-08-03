package ir.sam.hearthstone.client.model.requests;

public class SelectMinion extends Request {
    private final int side, index, emptyIndex;

    public SelectMinion(int side, int index, int emptyIndex) {
        this.side = side;
        this.index = index;
        this.emptyIndex = emptyIndex;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectMinion(side, index, emptyIndex);
    }
}
