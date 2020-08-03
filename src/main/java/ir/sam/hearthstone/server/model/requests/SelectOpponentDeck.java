package ir.sam.hearthstone.server.model.requests;

public class SelectOpponentDeck extends Request {
    private final String deckName;

    public SelectOpponentDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectOpponentDeck(deckName);
    }
}
