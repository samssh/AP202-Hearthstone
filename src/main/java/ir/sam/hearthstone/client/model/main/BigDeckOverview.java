package ir.sam.hearthstone.client.model.main;

import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

@ToString(includeFieldNames = false)
public class BigDeckOverview extends Overview {
    private String cardName;
    private int games, wins;
    private double winRate, manaAverage;
    @ToString.Exclude()
    private BufferedImage image;

    public BigDeckOverview(String name, String imageName) {
        super(name, imageName);
    }


    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest Expanded", Font.PLAIN, 20));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 40);
        g.drawString("hero name:" + imageName, 0, 80);
        String s;
        if (winRate != -1) s = new DecimalFormat("#.##").format(winRate);
        else s = "--";
        g.drawString("wins:" + wins + " games:" + games + " winRate:" + s, 0, 120);
        String p;
        if (manaAverage != 1000) {
            p = new DecimalFormat("#.##").format(manaAverage);
            g.drawString("mana average:" + p, 0, 160);
            g.drawString("MVC:" + cardName, 0, 200);
        } else g.drawString("deck empty", 0, 160);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
