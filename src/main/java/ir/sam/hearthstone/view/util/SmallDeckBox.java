package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.SmallDeckOverview;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SmallDeckBox extends Box<SmallDeckOverview, SmallDeckBox.SmallDeckViewer> {
    public SmallDeckBox(int width, int height, JPanel parent, MyActionListener deckActionListener) {
        super(width, height, parent, deckActionListener, Constant.SMALL_DECK_WIDTH, Constant.SMALL_DECK_HEIGHT, Constant.SMALL_DECK_SPACE);
    }

    @Override
    protected SmallDeckViewer createNew() {
        return new SmallDeckViewer();
    }

    @Override
    protected void set(SmallDeckViewer smallDeckViewer, SmallDeckOverview smallDeckOverview) {
        smallDeckViewer.smallDeckOverview = smallDeckOverview;
    }


    public class SmallDeckViewer extends JPanel implements MouseListener {
        private SmallDeckOverview smallDeckOverview;

        SmallDeckViewer() {
            this.setSize(Constant.SMALL_DECK_WIDTH, Constant.SMALL_DECK_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (smallDeckOverview != null)
                smallDeckOverview.paint((Graphics2D) g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null && smallDeckOverview!=null)
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

