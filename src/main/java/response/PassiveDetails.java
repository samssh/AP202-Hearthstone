package response;

import client.Client;
import lombok.Getter;
import util.ResponseLogInfoVisitor;
import util.Visitable;
import view.model.PassiveOverview;

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