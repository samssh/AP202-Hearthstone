package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Passive;
import ir.sam.hearthstone.resource_manager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PassiveOverview extends Overview{
    private final BufferedImage image;

    public PassiveOverview(Passive p) {
        super(p.getName(),p.getName());
        image = ImageLoader.getInstance().getPassive(imageName);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null);
    }

    @Override
    public String toString() {
        return "PassiveOverview(" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ')';
    }
}
