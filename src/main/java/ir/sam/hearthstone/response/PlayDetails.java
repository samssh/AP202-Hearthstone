package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.HeroOverview;
import ir.sam.hearthstone.view.model.HeroPowerOverview;
import lombok.Getter;
import ir.sam.hearthstone.util.Visitable;

import java.util.List;

public class PlayDetails extends Response implements Visitable<ResponseLogInfoVisitor> {
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
    public void execute(Client client) {
        client.setPlayDetail(hand, ground, weapon, hero, heroPower, eventLog, mana, deckCards);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setPlayDetailsInfo(this);
    }
}