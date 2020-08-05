package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PassiveOverview extends Overview {
    private transient BufferedImage image;

    public PassiveOverview() {
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = ImageLoader.getInstance().getPassive(imageName);
        }
        return image;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), 0, 0, null);
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
        return "PassiveOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
