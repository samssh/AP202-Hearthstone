package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.util.Visitable;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.PassiveOverview;
import ir.sam.hearthstone.view.model.SmallDeckOverview;
import lombok.Getter;

import java.util.List;

public class PassiveDetails extends Response implements Visitable<ResponseLogInfoVisitor> {
    @Getter
    private final List<PassiveOverview> passives;
    private final List<SmallDeckOverview> decks;
    private final List<CardOverview> cards;
    private final String message;
    private final boolean showButton;

    public PassiveDetails(List<PassiveOverview> passives, List<SmallDeckOverview> decks, List<CardOverview> cards, String message) {
        this(passives,decks,cards,message,false);
    }
    public PassiveDetails(List<PassiveOverview> passives, List<SmallDeckOverview> decks
            , List<CardOverview> cards, String message, boolean showButton) {
        this.passives = passives;
        this.decks = decks;
        this.cards = cards;
        this.message = message;
        this.showButton = showButton;
    }

    @Override
    public void execute(Client client) {
        client.setPassives(passives,decks,cards,message,showButton);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setPassiveDetailsInfo(this);
    }
}