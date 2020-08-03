package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;

public class CollectionFilter extends Request {
    @Getter
    private final String name, classOfCard;
    @Getter
    private final int mana, lockMode;

    public CollectionFilter(String name, String classOfCard, int mana, int lockMode) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.applyCollectionFilter(name, classOfCard, mana, lockMode);
    }
}