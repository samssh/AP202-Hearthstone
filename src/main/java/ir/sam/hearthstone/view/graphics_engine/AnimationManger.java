package ir.sam.hearthstone.view.graphics_engine;

import ir.sam.hearthstone.view.graphics_engine.effects.PaintByTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationManger {
    private final List<PaintByTime> painters;
    private volatile long lastPaint;
    private EndAnimationAction endAnimationAction;

    public AnimationManger() {
        painters = new ArrayList<>();
        lastPaint = 0;
    }

    public void addPainter(PaintByTime painter) {
        synchronized (painters) {
            painters.add(painter);
        }
    }

    public void clear() {
        lastPaint = 0;
        synchronized (painters) {
            painters.clear();
        }
    }

    public void start(EndAnimationAction endAnimationAction) {
        this.endAnimationAction = endAnimationAction;
        lastPaint = System.nanoTime();
    }

    public void start(){
        start(null);
    }

    public void paint(Graphics2D graphics2D) {
        if (lastPaint == 0) return;
        double time = (System.nanoTime() - lastPaint) / (1e9);
        if (time >= 1) {
            if (endAnimationAction != null)
                endAnimationAction.action();
            clear();
            return;
        }
        synchronized (painters) {
            painters.forEach(painter -> painter.paint(graphics2D, time));
        }

    }
}
