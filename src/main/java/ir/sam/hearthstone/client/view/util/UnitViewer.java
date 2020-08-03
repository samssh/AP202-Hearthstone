package ir.sam.hearthstone.client.view.util;

import ir.sam.hearthstone.client.model.main.UnitOverview;
import ir.sam.hearthstone.client.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.client.view.graphics_engine.effects.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class UnitViewer extends JPanel {
    @Getter
    protected UnitOverview unitOverview;
    @Setter
    protected MyActionListener actionListener;
    protected final JPanel parent;

    protected final AnimationManger animationManger;


    public UnitViewer(JPanel parent) {
        this.setLayout(null);
        this.parent = parent;
        this.setOpaque(false);
        this.addMouseListener((MyMouseListener) this::mouseClicked);
        animationManger = new AnimationManger();
    }

    public UnitViewer(UnitOverview unitOverview, JPanel parent) {
        this(parent);
        this.unitOverview = unitOverview;
        if (unitOverview.getToolkit() != null)
            setToolTipText(unitOverview.getToolkit());
    }

    public UnitViewer(JPanel parent, MyActionListener actionListener) {
        this(parent);
        this.actionListener = actionListener;
    }

    public void setUnitOverview(UnitOverview unitOverview) {
        this.unitOverview = unitOverview;
        if (unitOverview != null && unitOverview.getToolkit() != null)
            setToolTipText(unitOverview.getToolkit());
    }

    public void setUnitOverviewAnimated(UnitOverview unitOverview) {
        PaintByTime painter;
        PaintByTime neW = new OverviewPainter(unitOverview);
        PaintByTime old = new OverviewPainter(this.unitOverview);
        if (this.unitOverview == null) painter = new SinglePictureScale(neW, false, ScaleOnCenter.ALL);
        else if (unitOverview == null) painter = new SinglePictureScale(old, true, ScaleOnCenter.ALL);
        else {
            painter = new DoublePictureScale(neW, old, ScaleOnCenter.X);
        }
        setUnitOverview(null);
        animationManger.addPainter(painter);
        animationManger.start(() -> setUnitOverview(unitOverview));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (unitOverview != null)
            unitOverview.paint((Graphics2D) g);
        animationManger.paint((Graphics2D) g);
    }


    public void mouseClicked(MouseEvent e) {
        if (unitOverview != null) {
            if (e.getButton() == MouseEvent.BUTTON3) JOptionPane.showMessageDialog(this.parent, null,
                    "information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(unitOverview.getBigImage()));
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (actionListener != null && unitOverview != null) {
                    actionListener.action(unitOverview.getName());
                }
            }
        }
    }
}