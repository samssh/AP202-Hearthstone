package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class HeroOverview extends UnitOverview {
    static final BufferedImage defImage = ImageLoader.getInstance().getEffect("defence");
    @Getter
    @Setter
    private int hp, defence;
    private transient BufferedImage big, small;

    public HeroOverview() {
    }

    public BufferedImage getBig() {
        if (big == null) {
            big = ImageLoader.getInstance().getBigHero(imageName);
        }
        return big;
    }

    public BufferedImage getSmall() {
        if (small == null) {
            small = ImageLoader.getInstance().getSmallHero(imageName);
        }
        return small;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(getSmall(), 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(19.5F));
        int w = getSmall().getWidth();
        int h = getSmall().getHeight();
        if (defence > 0) {
            g.drawImage(defImage, 87 * w / 120, 65 * h / 170, null);
            g.drawString(defence + "", 96 * w / 120, 88 * h / 170);
        }
        g.drawString(hp + "", 93 * w / 120, 122 * h / 170);
    }

    @Override
    public int getWidth() {
        return getSmall().getWidth();
    }

    @Override
    public int getHeight() {
        return getSmall().getHeight();
    }

    @Override
    public Image getBigImage() {
        ColorModel cm = getBig().getColorModel();
        BufferedImage image = new BufferedImage(cm, getBig().copyData(null), cm.isAlphaPremultiplied(), null);
        Graphics g = image.createGraphics();
        this.paintBig(g);
        return image;
    }

    private void paintBig(Graphics g) {
        int w = getBig().getWidth();
        int h = getBig().getHeight();
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        g.drawString(hp + "", 192 * w / 250, 250 * h / 350);
    }

    @Override
    public String toString() {
        return "HeroOverview{" +
                "hp=" + hp +
                ", defence=" + defence +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
