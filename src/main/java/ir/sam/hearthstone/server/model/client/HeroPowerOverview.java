package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.main.HeroPower;
import lombok.ToString;

@ToString(includeFieldNames = false)
public class HeroPowerOverview extends UnitOverview {
    private final int mana;

    public HeroPowerOverview(HeroPower heroPower) {
        super(heroPower.getName(), heroPower.getName(), null);
        mana = heroPower.getManaFrz();

    }
}
