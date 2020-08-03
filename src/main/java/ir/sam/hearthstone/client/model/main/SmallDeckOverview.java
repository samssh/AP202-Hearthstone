package ir.sam.hearthstone.client.model.main;

import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;

@ToString(includeFieldNames = false)
public class SmallDeckOverview extends Overview {
    @ToString.Exclude
    private BufferedImage image;

    public SmallDeckOverview(String name, String imageName) {
        super(name, imageName);
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
}
