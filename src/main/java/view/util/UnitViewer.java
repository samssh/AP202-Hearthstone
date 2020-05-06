package view.util;

import view.model.UnitOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UnitViewer extends JPanel implements MouseListener {
    private final UnitOverview unitOverview;
    private final JPanel parent;
    private MyActionListener actionListener;


    UnitViewer(UnitOverview unitOverview, JPanel parent) {
        this.parent = parent;
        this.unitOverview = unitOverview;
        if (unitOverview.getToolkit() != null)
            setToolTipText(unitOverview.getToolkit());

        this.setSize(Constant.CARD_WIDTH, Constant.CARD_HEIGHT);
        this.setOpaque(false);
        this.addMouseListener(this);
    }

    public UnitViewer setActionListener(MyActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        unitOverview.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JOptionPane.showMessageDialog(this.parent, null,
                    "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(unitOverview.getBigImage()));
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (actionListener != null) {
                actionListener.action(unitOverview.getName());
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