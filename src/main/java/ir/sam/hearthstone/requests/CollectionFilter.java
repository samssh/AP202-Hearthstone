package ir.sam.hearthstone.requests;

import lombok.Getter;
import ir.sam.hearthstone.server.Server;

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
    public void execute(Server server) {
        server.applyCollectionFilter(name, classOfCard, mana, lockMode);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setCollectionDetails(this);
    }
}