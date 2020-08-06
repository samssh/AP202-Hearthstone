package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.main.HeroPower;
import lombok.Getter;
import lombok.ToString;

public class HeroPowerOverview extends UnitOverview {
    @Getter
    private final int mana;

    public HeroPowerOverview(HeroPower heroPower) {
        super(heroPower.getName(), heroPower.getName(), null);
        mana = heroPower.getManaFrz();
    }

    @Override
    public String toString() {
        return "HeroPowerOverview{" +
                "mana=" + mana +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
