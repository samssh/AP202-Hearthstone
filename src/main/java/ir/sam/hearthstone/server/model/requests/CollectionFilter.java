package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class CollectionFilter extends Request {
    @Getter
    @Setter
    private String name, classOfCard;
    @Getter
    @Setter
    private int mana, lockMode;

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.applyCollectionFilter(name, classOfCard, mana, lockMode);
    }
}