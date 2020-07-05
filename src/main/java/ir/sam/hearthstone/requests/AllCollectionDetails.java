package ir.sam.hearthstone.requests;

import ir.sam.hearthstone.server.Server;
import lombok.Getter;

public class AllCollectionDetails extends Request{
    @Getter
    private final String name, classOfCard;
    @Getter
    private final int mana, lockMode;

    public AllCollectionDetails(String name, String classOfCard, int mana, int lockMode) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.mana = mana;
        this.lockMode = lockMode;
    }

    @Override
    public void execute(Server server) {
        server.sendAllCollectionDetails(name,classOfCard,mana,lockMode);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {

    }
}
