package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Weapon;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import lombok.Getter;
import ir.sam.hearthstone.model.main.Minion;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
@ToString(exclude = {"big","small"})
public class CardOverview extends UnitOverview {
    @Getter
    private final int number;
    private final int price, mana, att, hp;
    private final boolean showPrice;
    private final BufferedImage big, small;

    public CardOverview(Card card, int number, boolean showPrice) {
        super(card.getName(), card.getName(), "class of card: " + card.getClassOfCard().getHeroName());
        this.number = number;
        this.price = card.getPrice();
        this.showPrice = showPrice;
        this.mana = card.getManaFrz();
        if (card instanceof Minion) {
            this.att = ((Minion) card).getAttFrz();
            this.hp = ((Minion) card).getHpFrz();
        } else if (card instanceof Weapon) {
            this.att = ((Weapon) card).getAttFrz();
            this.hp = ((Weapon) card).getUsage();
        } else {
            this.att = -1;
            this.hp = -1;
        }

        if (this.number > 0) {
            big = ImageLoader.getInstance().getBigCard(imageName);
            small = ImageLoader.getInstance().getSmallCard(imageName);
        } else {
            big = ImageLoader.getInstance().getBigGrayCard(imageName);
            small = ImageLoader.getInstance().getSmallGrayCard(imageName);
        }
    }

    @Override
    public void paint(Graphics g) {
        if (number == 2)
            g.drawImage(small, 15, 0, null);
        g.drawImage(small, 0, 0, null);
        int w = small.getWidth();
        int h = small.getHeight();
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

    private void paintBig(Graphics g) {
        int w = big.getWidth();
        int h = big.getHeight();
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
        ColorModel cm = big.getColorModel();
        BufferedImage image = new BufferedImage(cm, big.copyData(null), cm.isAlphaPremultiplied(), null);
        Graphics g = image.createGraphics();
        this.paintBig(g);
        return image;
    }
}
