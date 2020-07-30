package ir.sam.hearthstone.requests;

import lombok.Getter;

public class ChangeHeroDeck extends Request {
    @Getter
    private final String deckName, heroName;

    public ChangeHeroDeck(String deckName, String heroName) {
        this.deckName = deckName;
        this.heroName = heroName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.changeHeroDeck(deckName, heroName);
    }
}
