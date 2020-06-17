package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.view.model.BigDeckOverview;
import lombok.Getter;

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