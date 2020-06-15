package requests;

import lombok.Getter;
import server.Server;

public class ChangeHeroDeck extends Request {
    @Getter
    private final String deckName, heroName;

    public ChangeHeroDeck(String deckName, String heroName) {
        this.deckName = deckName;
        this.heroName = heroName;
    }

    @Override
    public void execute(Server server) {
        server.changeHeroDeck(deckName, heroName);
    }

    @Override
    public void accept(RequestLogInfoVisitor requestLogInfoVisitor) {
        requestLogInfoVisitor.setChangeHeroDeck(this);
    }
}
