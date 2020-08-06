package ir.sam.hearthstone.server.model.requests;

import lombok.Getter;
import lombok.Setter;

public class ChangeHeroDeck extends Request {
    @Getter
    @Setter
    private String deckName, heroName;

    public ChangeHeroDeck(String deckName, String heroName) {
        this.deckName = deckName;
        this.heroName = heroName;
    }

    @Override
    public void execute(RequestExecutor requestExecutor) {
        requestExecutor.changeHeroDeck(deckName, heroName);
    }
}
