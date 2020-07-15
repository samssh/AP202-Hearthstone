package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import lombok.Setter;
import ir.sam.hearthstone.view.model.UnitOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UnitViewer extends JPanel {
    protected UnitOverview unitOverview;
    @Setter
    protected MyActionListener actionListener;
    protected final JPanel parent;

    protected final AnimationManger animationManger;


    public UnitViewer(JPanel parent) {
        this.setLayout(null);
        this.parent = parent;
        this.setOpaque(false);
        this.addMouseListener((MyMouseListener)this::mouseClicked);
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
        animationManger.clear();
        PaintByTime painter,ove = new OverviewPainter(unitOverview);
        if (this.unitOverview==null) painter = new SinglePictureScale(ove,false,ScaleOnCenter.ALL);
        else {
            PaintByTime old = new OverviewPainter(this.unitOverview);
            painter = new DoublePictureScale(ove,old,ScaleOnCenter.X);
        }
        animationManger.addPainter(painter);
        animationManger.start(()->setUnitOverview(unitOverview));
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