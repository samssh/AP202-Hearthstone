package requests;

import lombok.Getter;
import server.Server;

public class CollectionDetails extends Request {
    @Getter
    private final String name, classOfCard, deckName;
    @Getter
    private final int mana, lockMode;

    public CollectionDetails(String name, String classOfCard, int mana, int lockMode, String deckName) {
        this.name = name;
        this.classOfCard = classOfCard;
        this.deckName = deckName;
        this.mana = mana;
        this.lockMode = lockMode;
    }

    @Override
    public void execute(Server server) {
        server.sendCollectionDetails(name, classOfCard, mana, lockMode, deckName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setCollectionDetails(this);
    }
}