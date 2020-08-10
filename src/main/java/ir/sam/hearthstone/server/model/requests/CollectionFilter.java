package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
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
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.applyCollectionFilter(name, classOfCard, mana, lockMode);
    }
}