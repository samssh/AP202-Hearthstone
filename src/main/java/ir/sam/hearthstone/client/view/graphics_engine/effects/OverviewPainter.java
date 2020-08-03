package ir.sam.hearthstone.client.view.graphics_engine.effects;

import ir.sam.hearthstone.client.view.model.Overview;

import java.awt.*;

public class OverviewPainter implements PaintByTime {
    private final Overview overview;

    public OverviewPainter(Overview overview) {
        this.overview = overview;
    }

    @Override
    public void paint(Graphics2D graphics2D, double time) {
        if (overview != null)
            overview.paint(graphics2D);
    }

    @Override
    public int getWidth() {
        return overview.getWidth();
    }

    @Override
    public int getHeight() {
        return overview.getHeight();
    }
}
