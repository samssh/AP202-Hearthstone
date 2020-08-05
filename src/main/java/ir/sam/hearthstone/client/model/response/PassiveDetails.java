package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.PassiveOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PassiveDetails extends Response {
    @Getter
    @Setter
    private List<PassiveOverview> passives;
    @Getter
    @Setter
    private List<SmallDeckOverview> decks;
    @Getter
    @Setter
    private List<CardOverview> cards;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private boolean showButton;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setPassives(passives, decks, cards, message, showButton);
    }
}