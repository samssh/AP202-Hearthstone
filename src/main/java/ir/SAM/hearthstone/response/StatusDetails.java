package ir.SAM.hearthstone.response;

import ir.SAM.hearthstone.client.Client;
import lombok.Getter;
import ir.SAM.hearthstone.view.model.BigDeckOverview;

import java.util.List;

public class StatusDetails extends Response {
    @Getter
    private final List<BigDeckOverview> bigDeckOverviews;

    public StatusDetails(List<BigDeckOverview> bigDeckOverviews) {
        this.bigDeckOverviews = bigDeckOverviews;
    }

    @Override
    public void execute(Client client) {
        client.setStatusDetails(bigDeckOverviews);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setStatusDetailsInfo(this);
    }
}