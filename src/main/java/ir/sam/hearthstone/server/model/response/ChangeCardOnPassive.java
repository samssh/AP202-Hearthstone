package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.CardOverview;
import lombok.Getter;

public class ChangeCardOnPassive extends Response {
    @Getter
    private final CardOverview cardOverview;
    @Getter
    private final int index;

    public ChangeCardOnPassive(CardOverview cardOverview, int index) {
        this.cardOverview = cardOverview;
        this.index = index;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.changeCardOnPassive(cardOverview, index);
    }
}
