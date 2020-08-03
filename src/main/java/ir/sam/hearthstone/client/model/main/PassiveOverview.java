package ir.sam.hearthstone.client.model.main;

import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;

@ToString(includeFieldNames = false)
public class PassiveOverview extends Overview {
    @ToString.Exclude
    private BufferedImage image;

    public PassiveOverview(String name, String imageName) {
        super(name, imageName);
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
}
