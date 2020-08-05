package ir.sam.hearthstone.client.model.main;

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


    @Override
    public void paint(Graphics2D g) {
        g.drawImage(small, 0, 0, null);
        if (mana > 0) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD));
            g.setFont(g.getFont().deriveFont(21F));
            int w = small.getWidth();
            int h = small.getHeight();
            g.drawString(mana + "", 55 * w / 120, 23 * h / 170);
        }
    }

    @Override
    public int getWidth() {
        return small.getWidth();
    }

    @Override
    public int getHeight() {
        return small.getHeight();
    }

    @Override
    public Image getBigImage() {
        if (mana > 0) {
            ColorModel cm = big.getColorModel();
            BufferedImage image = new BufferedImage(cm, big.copyData(null), cm.isAlphaPremultiplied(), null);
            Graphics g = image.createGraphics();
            this.paintBig(g);
            return image;
        } else return big;
    }

    private void paintBig(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        int w = big.getWidth();
        int h = big.getHeight();
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
