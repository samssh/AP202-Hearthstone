package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Passive;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;

@ToString(includeFieldNames = false)
public class PassiveOverview extends Overview {
    @ToString.Exclude
    private final BufferedImage image;

    public PassiveOverview(Passive p) {
        super(p.getName(), p.getName());
        image = ImageLoader.getInstance().getPassive(imageName);
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
