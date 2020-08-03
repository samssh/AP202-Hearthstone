package ir.sam.hearthstone.server.model.client;

import lombok.Getter;

public abstract class UnitOverview extends Overview {
    @Getter
    private final String toolkit;

    public UnitOverview(String name, String imageName, String toolkit) {
        super(name, imageName);
        this.toolkit = toolkit;
    }
}
