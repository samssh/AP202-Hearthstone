package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.util.Visitable;
import ir.sam.hearthstone.view.model.PassiveOverview;
import lombok.Getter;

import java.util.List;

public class PassiveDetails extends Response implements Visitable<ResponseLogInfoVisitor> {
    @Getter
    private final List<PassiveOverview> passives;

    public PassiveDetails(List<PassiveOverview> passives) {
        this.passives = passives;
    }

    @Override
    public void execute(Client client) {
        client.setPassives(passives);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setPassiveDetailsInfo(this);
    }
}