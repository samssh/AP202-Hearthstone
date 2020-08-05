package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.BigDeckOverview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class StatusDetails extends Response {
    @Getter
    @Setter
    private List<BigDeckOverview> bigDeckOverviews;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setStatusDetails(bigDeckOverviews);
    }
}