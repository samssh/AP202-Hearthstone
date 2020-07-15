package ir.sam.hearthstone.view.graphics_engine;

import ir.sam.hearthstone.view.graphics_engine.effects.PaintByTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationManger {
    private final static EndAnimationAction dummyAction = () -> {
    };
    private final List<PaintByTime> painters;
    private volatile long lastPaint;
    private final List<EndAnimationAction> endAnimationActions;

    public AnimationManger() {
        endAnimationActions = new ArrayList<>();
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
        endAnimationActions.forEach(EndAnimationAction::action);
        endAnimationActions.clear();
        synchronized (painters) {
            painters.clear();
        }
    }

    public void start(EndAnimationAction endAnimationAction) {
        this.endAnimationActions.forEach(EndAnimationAction::action);
        this.endAnimationActions.clear();
        this.endAnimationActions.add(endAnimationAction);
        lastPaint = System.nanoTime();
    }

    public void start() {
        start(dummyAction);
    }

    public void addEndAnimationAction(EndAnimationAction endAnimationAction){
        this.endAnimationActions.add(endAnimationAction);
    }

    public void paint(Graphics2D graphics2D) {
        if (lastPaint == 0) return;
        double time = (System.nanoTime() - lastPaint) / (1e9);
        if (time >= 1) {
            clear();
            return;
        }
        synchronized (painters) {
            painters.forEach(painter -> painter.paint(graphics2D, time));
        }

    }
}
