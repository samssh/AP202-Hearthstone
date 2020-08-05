package ir.sam.hearthstone.client.model.main;


import ir.sam.hearthstone.client.resource_manager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SmallDeckOverview extends Overview {
    private transient BufferedImage image;

    public SmallDeckOverview() {
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = ImageLoader.getInstance().getSmallDeck(imageName);
        }
        return image;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), 0, 0, null);
        g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 20);
        g.drawString("hero name:" + imageName, 0, 40);
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
        return "SmallDeckOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
