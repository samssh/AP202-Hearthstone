package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class HeroPowerOverview extends UnitOverview {
    @Getter
    @Setter
    private int mana;
    private transient BufferedImage big, small;

    public HeroPowerOverview() {
    }

    public BufferedImage getBig() {
        if (big == null) {
            big = ImageLoader.getInstance().getBigHeroPower(imageName);
        }
        return big;
    }

    public BufferedImage getSmall() {
        if (small == null) {
            small = ImageLoader.getInstance().getSmallHeroPower(imageName);
        }
        return small;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(getSmall(), 0, 0, null);
        if (mana > 0) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD));
            g.setFont(g.getFont().deriveFont(21F));
            int w = getSmall().getWidth();
            int h = getSmall().getHeight();
            g.drawString(mana + "", 55 * w / 120, 23 * h / 170);
        }
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
        if (mana > 0) {
            ColorModel cm = getBig().getColorModel();
            BufferedImage image = new BufferedImage(cm, getBig().copyData(null), cm.isAlphaPremultiplied(), null);
            Graphics g = image.createGraphics();
            this.paintBig(g);
            return image;
        } else return getBig();
    }

    private void paintBig(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        int w = getBig().getWidth();
        int h = getBig().getHeight();
        g.drawString(mana + "", 115 * w / 250, 46 * h / 350);
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
