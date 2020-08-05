package ir.sam.hearthstone.client.model.main;


import java.awt.*;
import java.awt.image.BufferedImage;

public class SmallDeckOverview extends Overview {
    private transient BufferedImage image;

    public SmallDeckOverview() {
    }


    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 20);
        g.drawString("hero name:" + imageName, 0, 40);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public String toString() {
        return "SmallDeckOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
