package view.util;

import view.model.SmallDeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static view.util.Constant.*;

public class SmallDeckBox extends Box<SmallDeckOverview, SmallDeckBox.SmallDeckViewer, DeckActionListener> {
    public SmallDeckBox(int width, int height, JPanel parent, DeckActionListener deckActionListener) {
        super(width, height, parent, deckActionListener, SMALL_DECK_WIDTH, SMALL_DECK_HEIGHT, SMALL_DECK_SPACE);
    }

    @Override
    protected SmallDeckViewer createNew(SmallDeckOverview deckOverview) {
        return new SmallDeckViewer(deckOverview);
    }


    public class SmallDeckViewer extends JPanel implements MouseListener {
        private final SmallDeckOverview smallDeckOverview;

        SmallDeckViewer(SmallDeckOverview smallDeckOverview) {
            this.smallDeckOverview = smallDeckOverview;
            this.setSize(Constant.SMALL_DECK_WIDTH, Constant.SMALL_DECK_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            smallDeckOverview.paint(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null)
                    action.action(smallDeckOverview.getName());
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

