package view.util;

import view.model.HeroOverview;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HeroViewer extends JPanel implements MouseListener {
    private MyActionListener actionListener;
    private final JPanel parent;
    private final HeroOverview heroOverview;
    public HeroViewer(HeroOverview heroOverview,JPanel parent){
        this.parent = parent;
        this.heroOverview = heroOverview;
        this.addMouseListener(this);
    }

    public void setActionListener(MyActionListener actionListener) {
        this.actionListener = actionListener;
    }



    @Override
    public void mouseClicked(MouseEvent e) {

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
