package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.client.PassiveOverview;
import ir.sam.hearthstone.server.model.client.SmallDeckOverview;
import lombok.Getter;

import java.util.List;

public class PassiveDetails extends Response {
    @Getter
    private final List<PassiveOverview> passives;
    private final List<SmallDeckOverview> decks;
    private final List<CardOverview> cards;
    private final String message;
    private final boolean showButton;

    public PassiveDetails(List<PassiveOverview> passives, List<SmallDeckOverview> decks, List<CardOverview> cards, String message) {
        this(passives, decks, cards, message, false);
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
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setPassives(passives, decks, cards, message, showButton);
    }
}