package ir.sam.hearthstone.client.view.graphics_engine.effects;

import java.awt.*;

public class Rotary implements PaintByTime {
    private final PaintByTime painter;

    public Rotary(PaintByTime painter) {
        this.painter = painter;
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        double rotationRequired = 2 * Math.PI * (time);
        double locationX = painter.getWidth() / 2.0;
        double locationY = painter.getHeight() / 2.0;
        graphics2D.rotate(rotationRequired, locationX, locationY);
        painter.paint(graphics2D, time);
        graphics2D.rotate(-1 * rotationRequired, locationX, locationY);
    }

    @Override
    public int getWidth() {
        return painter.getWidth();
    }

    @Override
    public int getHeight() {
        return painter.getHeight();
    }
}
