package ir.sam.hearthstone.view.graphics_engine.effects;

import java.awt.*;

public class DoublePictureScale extends ScaleOnCenter {
    private final PaintByTime back, front;

    public DoublePictureScale(PaintByTime front, PaintByTime back, int mode) {
        super(mode);
        this.back = back;
        this.front = front;
    }

    public void paint(Graphics2D graphics2D, double time) {
        double scale;
        PaintByTime painter;
        if (time < 0.5) {
            scale = (1 - 2 * time);
            painter = back;
        } else {
            scale = 2 * time - 1;
            painter = front;
        }
        paint0(graphics2D, time, painter, scale);
    }

    @Override
    public int getWidth() {
        return front.getWidth();
    }

    @Override
    public int getHeight() {
        return front.getHeight();
    }
}
