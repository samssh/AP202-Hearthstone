package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import lombok.Getter;
import lombok.Setter;

public class AllCollectionDetails extends Request {
    @Getter
    @Setter
    private String name, classOfCard;
    @Getter
    @Setter
    private int mana, lockMode;

    @Override
    public void execute(RequestExecutor requestExecutor) throws DatabaseDisconnectException {
        requestExecutor.sendAllCollectionDetails(name, classOfCard, mana, lockMode);
    }
}
