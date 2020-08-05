package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class CardOverview extends UnitOverview {
    @Getter
    @Setter
    protected int number, price, mana, att, hp;
    @Getter
    @Setter
    protected boolean showPrice;
    protected transient BufferedImage big, small;

    public CardOverview() {
    }

    private CardOverview(String name, String imageName, String toolkit, int number, int price
            , int mana, int att, int hp, boolean showPrice, BufferedImage big, BufferedImage small) {
        this.name = name;
        this.imageName = imageName;
        this.toolkit = toolkit;
        this.number = number;
        this.price = price;
        this.mana = mana;
        this.att = att;
        this.hp = hp;
        this.showPrice = showPrice;
        this.big = big;
        this.small = small;
    }

    public CardOverview getClone() {
        return new CardOverview(name, imageName, getToolkit(), number, price, mana, att
                , hp, showPrice, getBig(), getSmall());
    }

    public BufferedImage getBig() {
        if (big == null) {
            if (this.number > 0) {
                big = ImageLoader.getInstance().getBigCard(imageName);
            } else {
                big = ImageLoader.getInstance().getBigGrayCard(imageName);
            }
        }
        return big;
    }

    public BufferedImage getSmall() {
        if (small == null) {
            if (this.number > 0) {
                small = ImageLoader.getInstance().getSmallCard(imageName);
            } else {
                small = ImageLoader.getInstance().getSmallGrayCard(imageName);
            }
        }
        return small;
    }

    @Override
    public void paint(Graphics2D g) {
        if (number == 2)
            g.drawImage(getSmall(), 15, 0, null);
        g.drawImage(getSmall(), 0, 0, null);
        int w = getSmall().getWidth();
        int h = getSmall().getHeight();
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(21.0F));
        g.drawString(mana + "", 10 * w / 120, 25 * h / 170);
        if (att >= 0 && hp >= 0) {
            g.drawString(att + "", 13 * w / 120, 161 * h / 170);
            g.drawString(hp + "", 100 * w / 120, 161 * h / 170);
        }
        if (showPrice) {
            g.setFont(g.getFont().deriveFont(18.0F));
            g.setColor(Color.RED);
            g.drawString("\u0024" + price, 60 * w / 120, 155 * h / 170);
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

    private void paintBig(Graphics g) {
        int w = getBig().getWidth();
        int h = getBig().getHeight();
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        g.drawString(mana + "", 25 * w / 250, 55 * h / 350);
        if (att >= 0 && hp >= 0) {
            g.drawString(att + "", 25 * w / 250, 327 * h / 350);
            g.drawString(hp + "", 205 * w / 250, 327 * h / 350);
        }
        if (showPrice) {
            g.setFont(g.getFont().deriveFont(25F).deriveFont(Font.BOLD));
            g.setColor(Color.RED);
            g.drawString("\u0024" + price, 115 * w / 250, 320 * h / 350);
        }
    }

    @Override
    public BufferedImage getBigImage() {
        ColorModel cm = getBig().getColorModel();
        BufferedImage image = new BufferedImage(cm, getBig().copyData(null), cm.isAlphaPremultiplied()
                , null);
        Graphics g = image.createGraphics();
        this.paintBig(g);
        return image;
    }

    @Override
    public String toString() {
        return "CardOverview{" +
                "number=" + number +
                ", price=" + price +
                ", mana=" + mana +
                ", att=" + att +
                ", hp=" + hp +
                ", showPrice=" + showPrice +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
