package ir.sam.hearthstone.view.graphics_engine.effects;

import java.awt.*;

public abstract class ScaleOnCenter implements PaintByTime {
    public static final int ALL = 3, X = 2, Y = 1, NONE = 0, RANDOM = 5;
    private final int mode;

    public static int getRandomMode() {
        return (int) (Math.random() * 3) + 1;
    }

    public ScaleOnCenter(int mode) {
        if (mode == RANDOM)
            mode = (int) (Math.random() * 3) + 1;
        this.mode = mode;
    }

    protected void paint0(Graphics2D graphics2D, double time, PaintByTime painter) {
        int translateX = ((mode & X) > 0) ? (int) ((painter.getWidth() * (1 - time)) / 2) : 0;
        int translateY = ((mode & Y) > 0) ? (int) ((painter.getHeight() * (1 - time)) / 2) : 0;
        double scaleX = ((mode & X) > 0) ? time : 1;
        double scaleY = ((mode & Y) > 0) ? time : 1;
        graphics2D.translate(translateX, translateY);
        graphics2D.scale(scaleX, scaleY);
        painter.paint(graphics2D, time);
        graphics2D.scale(1 / scaleX, 1 / scaleY);
        graphics2D.translate(-1 * translateX, -1 * translateY);
    }
}
