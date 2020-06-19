package ir.sam.hearthstone.view.graphics_engine.effects;

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
        double scale = time;
        if (out) scale = 1-scale;
        paint0(graphics2D,time,painter,scale);
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
