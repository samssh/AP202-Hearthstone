package ir.sam.hearthstone.client.view.graphics_engine.effects;

import java.awt.*;

public class SinglePictureScale extends ScaleOnCenter {
    private final PaintByTime painter;
    private final boolean out;

    public SinglePictureScale(PaintByTime painter, boolean out, int mode) {
        super(mode);
        this.painter = painter;
        this.out = out;
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        if (out) time = 1 - time;
        paint0(graphics2D, time, painter);
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
