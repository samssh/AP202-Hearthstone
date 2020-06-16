package ir.SAM.hearthstone.view.util;

import lombok.Setter;
import ir.SAM.hearthstone.view.model.UnitOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UnitViewer extends JPanel implements MouseListener {
    private UnitOverview unitOverview;
    @Setter
    private MyActionListener actionListener;
    private final JPanel parent;


    public UnitViewer(JPanel parent) {
        this.setLayout(null);
        this.parent = parent;
        this.setOpaque(false);
        this.addMouseListener(this);
    }

    public UnitViewer(UnitOverview unitOverview, JPanel parent) {
        this(parent);
        this.unitOverview = unitOverview;
        if (unitOverview.getToolkit() != null)
            setToolTipText(unitOverview.getToolkit());
    }

    public UnitViewer(UnitOverview unitOverview, JPanel parent, MyActionListener actionListener) {
        this(unitOverview, parent);
        this.actionListener = actionListener;
    }
    public void setUnitOverview(UnitOverview unitOverview) {
        this.unitOverview = unitOverview;

        if (unitOverview!=null&&unitOverview.getToolkit() != null)
            setToolTipText(unitOverview.getToolkit());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (unitOverview != null)
            unitOverview.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (unitOverview != null) {
            if (e.getButton() == MouseEvent.BUTTON3) JOptionPane.showMessageDialog(this.parent, null,
                    "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(unitOverview.getBigImage()));
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (actionListener != null) {
                    actionListener.action(unitOverview.getName());
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}