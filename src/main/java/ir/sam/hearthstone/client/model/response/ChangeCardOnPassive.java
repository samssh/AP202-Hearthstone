package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;

public class ChangeCardOnPassive extends Response {
    private final CardOverview cardOverview;
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
