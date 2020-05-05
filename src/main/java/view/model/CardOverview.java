package view.model;

import lombok.Getter;
import model.Card;
import model.Minion;
import model.Weapon;
import util.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CardOverview extends Overview {
    @Getter
    private final String classOfCard;
    @Getter
    private final int number;
    private final int price, mana, att, hp;
    private final boolean showPrice;

    public CardOverview(Card card, int number, boolean showPrice) {
        super(card.getName(), card.getName());
        this.number = number;
        this.price = card.getPrice();
        this.classOfCard = card.getClassOfCard().getHeroName();
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
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image;
        if (number > 0) {
            image = ImageLoader.getInstance().getSmallCard(imageName);
        } else {
            image = ImageLoader.getInstance().getSmallGrayCard(imageName);
        }
        if (number == 2)
            g.drawImage(image, 15, 0, null);
        g.drawImage(image, 0, 0, null);
        int w = g.getClipBounds().width;
        int h = g.getClipBounds().height;
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(21.0F));
        g.drawString(mana + "", 10 * w / 135, 25 * h / 170);
        if (att >= 0 && hp >= 0) {
            g.drawString(att + "", 13 * w / 135, 161 * h / 170);
            g.drawString(hp + "", 100 * w / 135, 161 * h / 170);
        }
        if (showPrice) {
            g.setFont(g.getFont().deriveFont(18.0F));
            g.setColor(Color.RED);
            g.drawString("\u0024" + price, 60 * w / 135, 155 * h / 170);
        }
    }

    public void paintBig(Graphics g) {
        int w = g.getClipBounds().width;
        int h = g.getClipBounds().height;
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
            g.drawString("\u0024" + price, 60 * w / 135, 155 * h / 170);
        }
    }
}
