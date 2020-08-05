package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.CardOverview;
import lombok.Getter;
import lombok.Setter;

public class ChangeCardOnPassive extends Response {
    @Getter
    @Setter
    private CardOverview cardOverview;
    @Getter
    @Setter
    private int index;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.changeCardOnPassive(cardOverview, index);
    }
}
