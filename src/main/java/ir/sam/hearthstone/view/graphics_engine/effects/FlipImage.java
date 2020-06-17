package ir.sam.hearthstone.view.graphics_engine.effects;

import java.awt.*;

public class FlipImage implements PaintByTime {
    public static final int ALL = 3, X = 2, Y = 1, NONE = 0;
    private final PaintByTime back, front;
    private final int mode;

    public FlipImage(PaintByTime front, PaintByTime back, int mode) {
        this.back = back;
        this.front = front;
        this.mode = mode;
    }

    public void paint(Graphics2D graphics2D, double time) {
        double scale;
        PaintByTime gear;
        if (time < 0.5) {
            scale = (1 - 2 * time);
            gear = back;
        } else {
            scale = 2 * time - 1;
            gear = front;
        }
        int translateX = ((mode & X) > 0) ? (int) ((gear.getWidth() * (1 - scale)) / 2) : 0;
        int translateY = ((mode & Y) > 0) ? (int) ((gear.getHeight() * (1 - scale)) / 2) : 0;
        double scaleX = ((mode & X) > 0) ? scale : 1;
        double scaleY = ((mode & Y) > 0) ? scale : 1;
        graphics2D.translate(translateX, translateY);
        graphics2D.scale(scaleX, scaleY);
        gear.paint(graphics2D, time);
        graphics2D.scale(1 / scaleX, 1 / scaleY);
        graphics2D.translate(-1 * translateX, -1 * translateY);
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
