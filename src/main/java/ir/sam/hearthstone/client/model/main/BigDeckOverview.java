package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class BigDeckOverview extends Overview {
    @Getter
    @Setter
    private String cardName;
    @Getter
    @Setter
    private int games, wins, cupEarned;
    @Getter
    @Setter
    private double winRate, manaAverage;

    private transient BufferedImage image;

    public BigDeckOverview() {
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = ImageLoader.getInstance().getBigDeck(imageName);
        }
        return image;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), 0, 0, null);
        g.setFont(new Font("War Priest Expanded", Font.PLAIN, 20));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 40);
        g.drawString("hero name:" + imageName, 0, 80);
        String s;
        if (winRate != -1) s = new DecimalFormat("#.##").format(winRate);
        else s = "--";
        g.drawString("wins:" + wins + " games:" + games + " winRate:" + s, 0, 120);
        if (manaAverage != 1000) {
            String p = new DecimalFormat("#.##").format(manaAverage);
            g.drawString("mana avg:" + p + " cup earned:" + cupEarned, 0, 160);
            g.drawString("MVC:" + cardName, 0, 200);
        } else {
            g.drawString("cup earned:" + cupEarned, 0, 160);
            g.drawString("deck empty", 0, 200);
        }
    }

    @Override
    public int getWidth() {
        return getImage().getWidth();
    }

    @Override
    public int getHeight() {
        return getImage().getHeight();
    }

    @Override
    public String toString() {
        return "BigDeckOverview{" +
                "cardName='" + cardName + '\'' +
                ", games=" + games +
                ", wins=" + wins +
                ", winRate=" + winRate +
                ", manaAverage=" + manaAverage +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
