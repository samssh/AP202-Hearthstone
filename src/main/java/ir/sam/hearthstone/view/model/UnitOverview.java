package ir.sam.hearthstone.view.model;

import lombok.Getter;

import java.awt.*;

public abstract class UnitOverview extends Overview {
    @Getter
    private final String toolkit;

    public UnitOverview(String name, String imageName, String toolkit) {
        super(name, imageName);
        this.toolkit = toolkit;
    }

    public abstract Image getBigImage();
}
