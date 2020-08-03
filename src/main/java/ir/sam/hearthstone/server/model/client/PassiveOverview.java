package ir.sam.hearthstone.server.model.client;


import ir.sam.hearthstone.server.model.main.Passive;
import lombok.ToString;

@ToString(includeFieldNames = false)
public class PassiveOverview extends Overview {
    public PassiveOverview(Passive p) {
        super(p.getName(), p.getName());
    }
}
