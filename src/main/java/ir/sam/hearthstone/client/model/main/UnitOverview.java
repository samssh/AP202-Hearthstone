package ir.sam.hearthstone.client.model.main;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public abstract class UnitOverview extends Overview {
    @Getter
    @Setter
    protected String toolkit;

    public UnitOverview() {
    }

    public abstract Image getBigImage();
}
