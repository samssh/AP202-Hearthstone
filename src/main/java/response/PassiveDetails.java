package response;

import client.Client;
import lombok.Getter;
import view.model.PassiveOverview;

import java.util.List;

public class PassiveDetails extends Response {
    @Getter
    private final List<PassiveOverview> passives;

    public PassiveDetails(List<PassiveOverview> passives) {
        this.passives = passives;
    }

    @Override
    public void execute() {
        Client.getInstance().setPassives(passives);
    }
}