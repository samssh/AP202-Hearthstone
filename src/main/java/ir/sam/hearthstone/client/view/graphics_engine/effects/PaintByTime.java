package ir.sam.hearthstone.client.view.graphics_engine.effects;

import java.awt.*;

public interface PaintByTime {
    void paint(Graphics2D graphics2D, double time);

    int getWidth();

    int getHeight();
}
