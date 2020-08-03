package ir.sam.hearthstone.client.view.util;

import ir.sam.hearthstone.client.model.main.SmallDeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

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

    @Override
    protected SmallDeckViewer[][] createTArray(int i, int j) {
        return new SmallDeckViewer[i][j];
    }


    public class SmallDeckViewer extends JPanel implements MyMouseListener {
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
                if (action != null && smallDeckOverview != null)
                    action.action(smallDeckOverview.getName());
            }
        }
    }
}

