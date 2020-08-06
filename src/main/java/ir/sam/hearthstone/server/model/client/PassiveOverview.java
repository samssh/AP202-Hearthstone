package ir.sam.hearthstone.server.model.client;


import ir.sam.hearthstone.server.model.main.Passive;
import lombok.ToString;

public class PassiveOverview extends Overview {
    public PassiveOverview(Passive p) {
        super(p.getName(), p.getName());
    }

    @Override
    public String toString() {
        return "PassiveOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
