package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.BigDeckOverview;
import lombok.Getter;

import java.util.List;

public class StatusDetails extends Response {
    @Getter
    private final List<BigDeckOverview> bigDeckOverviews;

    public StatusDetails(List<BigDeckOverview> bigDeckOverviews) {
        this.bigDeckOverviews = bigDeckOverviews;
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setStatusDetails(bigDeckOverviews);
    }
}