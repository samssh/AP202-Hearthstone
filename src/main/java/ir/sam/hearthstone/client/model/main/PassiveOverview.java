package ir.sam.hearthstone.client.model.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PassiveOverview extends Overview {
    private transient BufferedImage image;

    public PassiveOverview() {
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
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
        return "PassiveOverview{" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
