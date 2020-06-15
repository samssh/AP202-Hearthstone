package view.model;

import model.main.Hero;
import resourceManager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

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
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(19.5F));
        int w = small.getWidth();
        int h = small.getHeight();
        g.drawString(hp+"",93*w/120,122*h/170);
    }

    @Override
    public Image getBigImage() {
        ColorModel cm = big.getColorModel();
        BufferedImage image = new BufferedImage(cm, big.copyData(null), cm.isAlphaPremultiplied(), null);
        Graphics g = image.createGraphics();
        this.paintBig(g);
        return image;
    }

    private void paintBig(Graphics g) {
        int w = big.getWidth();
        int h = big.getHeight();
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        g.drawString(hp+"",192*w/250,250*h/350);
    }

    @Override
    public String toString() {
        return "HeroOverview(" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", hp=" + hp +
                ')';
    }
}
