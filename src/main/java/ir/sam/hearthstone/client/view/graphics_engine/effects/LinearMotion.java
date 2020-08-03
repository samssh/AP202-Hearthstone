package ir.sam.hearthstone.client.view.graphics_engine.effects;

import ir.sam.hearthstone.client.view.graphics_engine.Speed;

import java.awt.*;

public class LinearMotion extends MovablePainter {

    public LinearMotion(int originX, int originY, int destinationX, int destinationY
            , PaintByTime painter, Speed speed) {
        super(originX, originY, destinationX, destinationY, painter, speed);
    }

    public LinearMotion(Point origin, Point destination, PaintByTime painter, Speed speed) {
        super(origin.x, origin.y, destination.x, destination.y, painter, speed);
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        double position = speed.getPosition(time);
        double x = originX + (destinationX - originX) * (position),
                y = originY + (destinationY - originY) * (position);
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
