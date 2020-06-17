package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.PassiveOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static ir.sam.hearthstone.view.util.Constant.*;

public class PassiveBox extends Box<PassiveOverview, PassiveBox.PassiveViewer>{

    public PassiveBox(int width, int height, JPanel parent, MyActionListener action) {
        super(width, height, parent, action, PASSIVE_WIDTH, PASSIVE_HEIGHT, PASSIVE_SPACE);
    }

    @Override
    protected PassiveViewer createNew(PassiveOverview passiveOverview) {
        return new PassiveViewer(passiveOverview);
    }

    class PassiveViewer extends JPanel implements MouseListener {
        private final PassiveOverview passiveOverview;

        private PassiveViewer(PassiveOverview passiveOverview) {
            this.passiveOverview = passiveOverview;
            this.setSize(PASSIVE_WIDTH, PASSIVE_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            passiveOverview.paint(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null)
                    action.action(passiveOverview.getName());
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
}
