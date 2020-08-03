package ir.sam.hearthstone.client.model.requests;

public class SelectDeck extends Request {
    private final String deckName;

    public SelectDeck(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.selectDeck(deckName);
    }
}