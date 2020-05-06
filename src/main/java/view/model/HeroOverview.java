package view.model;

import model.Hero;

import java.awt.*;

public class HeroOverview extends UnitOverview {
    private final int hp;

    public HeroOverview(Hero h) {
        super(h.getName(), h.getName(), null);
        hp = h.getHpFrz();
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public Image getBigImage() {
        return null;
    }
}
