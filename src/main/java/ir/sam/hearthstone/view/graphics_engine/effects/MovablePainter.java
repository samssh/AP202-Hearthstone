package ir.sam.hearthstone.view.graphics_engine.effects;

import ir.sam.hearthstone.view.graphics_engine.Speed;

public abstract class MovablePainter  implements PaintByTime {
    protected final int originX, originY, destinationX, destinationY;
    protected final PaintByTime painter;
    protected final Speed speed;

    public MovablePainter(int originX, int originY, int destinationX, int destinationY
            , PaintByTime painter, Speed speed) {
        this.originX = originX;
        this.originY = originY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.painter = painter;
        this.speed = speed;
    }
}
