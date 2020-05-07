package view.model;

import model.main.Hero;
import util.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroOverview extends UnitOverview {
    private final int hp;
    private final BufferedImage big, small;

    public HeroOverview(Hero h) {
        super(h.getName(), h.getName(), null);
        hp = h.getHpFrz();
        small = ImageLoader.getInstance().getSmallHero(imageName);
        big = ImageLoader.getInstance().getBigHero(imageName);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(small,0,0,null);
    }

    @Override
    public Image getBigImage() {
        return big;
    }
}
