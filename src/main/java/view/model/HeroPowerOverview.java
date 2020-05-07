package view.model;

import model.main.HeroPower;
import util.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroPowerOverview extends UnitOverview {
    private final int mana;
    private final BufferedImage big, small;

    public HeroPowerOverview(HeroPower heroPower) {
        super(heroPower.getName(), heroPower.getName(), null);
        mana = heroPower.getManaFrz();
        small = ImageLoader.getInstance().getSmallHeroPower(imageName);
        big = ImageLoader.getInstance().getBigHeroPower(imageName);
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
