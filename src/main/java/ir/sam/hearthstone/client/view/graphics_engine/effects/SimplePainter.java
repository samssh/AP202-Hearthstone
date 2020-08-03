package ir.sam.hearthstone.client.view.graphics_engine.effects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimplePainter implements PaintByTime {
    private final BufferedImage image;

    public SimplePainter(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        graphics2D.drawImage(image, 0, 0, null);
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
