package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.Painter;

import java.awt.*;

public class TranslatorByTime implements Painter {
    private final Painter painter;
    private final int x, y;

    public TranslatorByTime(Painter painter, int x, int y) {
        this.painter = painter;
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.translate(x, y);
        painter.paint(graphics2D);
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
