package response;

import client.Client;
import lombok.Getter;
import view.model.CardOverview;
import view.model.HeroOverview;
import view.model.HeroPowerOverview;

import java.util.List;

public class PlayDetails extends Response {
    @Getter
    private final List<CardOverview> hand, ground;
    @Getter
    private final CardOverview weapon;
    @Getter
    private final HeroOverview hero;
    @Getter
    private final HeroPowerOverview heroPower;
    @Getter
    private final String eventLog;
    @Getter
    private final int mana, deckCards;

    public PlayDetails(List<CardOverview> hand, List<CardOverview> ground, CardOverview weapon,
                       HeroOverview hero, HeroPowerOverview heroPower, String eventLog, int mana, int deckCards) {
        this.hand = hand;
        this.ground = ground;
        this.weapon = weapon;
        this.hero = hero;
        this.heroPower = heroPower;
        this.eventLog = eventLog;
        this.mana = mana;
        this.deckCards = deckCards;
    }

    @Override
    public void execute() {
        Client.getInstance().setPlayDetail(hand, ground, weapon, hero, heroPower, eventLog, mana, deckCards);
    }
}