package requests;

import lombok.Getter;
import server.Server;

public class NewDeck extends Request {
    @Getter
    private final String deckName, heroName;

    public NewDeck(String deckName, String heroName) {
        this.deckName = deckName;
        this.heroName = heroName;
    }

    @Override
    public void execute() {
        Server.getInstance().newDeck(deckName, heroName);
    }
}