package ir.sam.hearthstone.client.view.graphics_engine;

import ir.sam.hearthstone.client.view.graphics_engine.effects.*;
import ir.sam.hearthstone.client.view.model.CardOverview;
import ir.sam.hearthstone.client.view.model.Overview;
import ir.sam.hearthstone.client.view.model.UnitOverview;
import ir.sam.hearthstone.client.view.util.Box;
import ir.sam.hearthstone.client.view.util.CardBox;
import ir.sam.hearthstone.client.view.util.UnitViewer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationManger {
    private final static EndAnimationAction dummyAction = () -> {
    };

    public static void moveCard(String cardName, CardBox origin, CardBox dest, AnimationManger animationManger) {
        Point org = origin.getPosition(cardName);
        org.translate(origin.getX(), origin.getY());
        CardOverview cardOverview = origin.removeModel(cardName, false);
        dest.addModel(cardOverview, false);
        Point des = dest.getPosition(cardName);
        des.translate(dest.getX(), dest.getY());
        animationManger.addPainter(new LinearMotion(org.x, org.y, des.x, des.y,
                new Rotary(new OverviewPainter(cardOverview)), x -> Math.pow(x, 1 / 2.5)));
        animationManger.start();
    }

    public static <T extends Overview, E extends Overview> void moveAndChangeCard(Box<T, ?> destBox
            , Box<E, ?> orgBox, T newUnit, int indexOnDest, int indexOnOrg, AnimationManger animationManger) {
        Point orgPoint = orgBox.getPosition(indexOnOrg);
        orgPoint.translate(orgBox.getX(), orgBox.getY());
        Point destPoint = destBox.getPosition(indexOnDest);
        destPoint.translate(destBox.getX(), destBox.getY());
        E oldUnit = orgBox.removeModel(indexOnOrg);
        PaintByTime old = new OverviewPainter(oldUnit);
        PaintByTime neW = new OverviewPainter(newUnit);
        PaintByTime painter = new DoublePictureScale(neW, old, ScaleOnCenter.ALL);
        painter = new Rotary(painter);
        painter = new LinearMotion(orgPoint.x, orgPoint.y, destPoint.x, destPoint.y, painter, x -> Math.pow(x, 1.2));
        animationManger.addPainter(painter);
        destBox.addModel(indexOnDest, newUnit);
    }

    public static void moveUnitToViewer(UnitViewer dest, Box<? extends UnitOverview, ?> orgBox
            , UnitOverview newUnit, int indexOnOrg, AnimationManger animationManger) {
        Point orgPoint = orgBox.getPosition(indexOnOrg);
        orgPoint.translate(orgBox.getX(), orgBox.getY());
        UnitOverview oldUnit = orgBox.removeModel(indexOnOrg);
        PaintByTime old = new OverviewPainter(oldUnit);
        PaintByTime neW = new OverviewPainter(newUnit);
        PaintByTime painter = new DoublePictureScale(neW, old, ScaleOnCenter.ALL);
        painter = new Rotary(painter);
        painter = new LinearMotion(orgPoint.x, orgPoint.y, dest.getX(), dest.getY(), painter, x -> Math.pow(x, 1.2));
        animationManger.addPainter(painter);
        animationManger.addEndAnimationAction(() -> dest.setUnitOverview(newUnit));
    }

    private final List<PainterGroup> painterGroups;
    private PainterGroup temp;

    public AnimationManger() {
        painterGroups = new ArrayList<>();
        temp = new PainterGroup();
    }

    public void addPainter(PaintByTime painter) {
        temp.painters.add(painter);
    }

    public void start(EndAnimationAction endAnimationAction) {
        temp.endAnimationActions.add(endAnimationAction);
        temp.lastPaint = System.nanoTime();
        synchronized (painterGroups) {
            painterGroups.add(temp);
        }
        temp = new PainterGroup();
    }

    public void start() {
        start(dummyAction);
    }

    public void addEndAnimationAction(EndAnimationAction endAnimationAction) {
        temp.endAnimationActions.add(endAnimationAction);
    }

    public void paint(Graphics2D graphics2D) {
        synchronized (painterGroups) {
            Iterator<PainterGroup> iterator = painterGroups.iterator();
            while (iterator.hasNext()) {
                PainterGroup painterGroup = iterator.next();
                double time = (System.nanoTime() - painterGroup.lastPaint) / (1e9);
                if (time >= 1) {
                    painterGroup.clear();
                    iterator.remove();
                    continue;
                }
                painterGroup.painters.forEach(painter -> painter.paint(graphics2D, time));
            }
        }
    }

    public void clear() {
        synchronized (painterGroups) {
            painterGroups.forEach(PainterGroup::clear);
            painterGroups.clear();
        }
    }

    private static class PainterGroup {
        private final List<PaintByTime> painters;
        private volatile long lastPaint;
        private final List<EndAnimationAction> endAnimationActions;

        private PainterGroup() {
            endAnimationActions = new ArrayList<>();
            painters = new ArrayList<>();
            lastPaint = 0;
        }

        public void clear() {
            endAnimationActions.forEach(EndAnimationAction::action);
        }
    }
}
