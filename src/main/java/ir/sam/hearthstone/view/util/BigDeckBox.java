package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.BigDeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BigDeckBox extends Box<BigDeckOverview, BigDeckBox.BigDeckViewer> {
    public BigDeckBox(int width, int height, JPanel parent, MyActionListener deckActionListener) {
        super(width, height, parent, deckActionListener, Constant.BIG_DECK_WIDTH, Constant.BIG_DECK_HEIGHT, Constant.BIG_DECK_SPACE);
    }

    @Override
    protected BigDeckViewer createNew(BigDeckOverview overview) {
        return new BigDeckViewer(overview);
    }


    class BigDeckViewer extends JPanel implements MouseListener {
        private final BigDeckOverview bigDeckOverview;

        private BigDeckViewer(BigDeckOverview bigDeckOverview) {
            this.bigDeckOverview = bigDeckOverview;
            this.setSize(Constant.BIG_DECK_WIDTH, Constant.BIG_DECK_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            bigDeckOverview.paint(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null)
                    action.action(bigDeckOverview.getName());
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
