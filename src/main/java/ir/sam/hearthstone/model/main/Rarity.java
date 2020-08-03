package ir.sam.hearthstone.model.main;

import lombok.Getter;

public enum Rarity {
    Common(1), Rare(2), Epic(3), Legendary(4);
    @Getter
    private final int i;

    Rarity(int i) {
        this.i = i;
    }
}
