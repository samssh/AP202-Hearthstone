package ir.sam.hearthstone.view.graphics_engine.effects;

import java.awt.*;

public class TranslatorByTime implements PaintByTime {
    private final PaintByTime painter;
    private final int x, y;

    public TranslatorByTime(PaintByTime painter, int x, int y) {
        this.painter = painter;
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        graphics2D.translate(x, y);
        painter.paint(graphics2D, time);
        graphics2D.translate(-1 * x, -1 * y);
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
