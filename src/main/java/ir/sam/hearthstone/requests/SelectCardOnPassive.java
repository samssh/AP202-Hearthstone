package ir.sam.hearthstone.requests;

public class SelectCardOnPassive extends Request {
    private final int index;

    public SelectCardOnPassive(int index) {
        this.index = index;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectCadOnPassive(index);
    }
}
