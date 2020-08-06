package ir.sam.hearthstone.server.model.requests;

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
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.sendAllCollectionDetails(name, classOfCard, mana, lockMode);
    }
}
