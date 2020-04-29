package view.util;

import util.ImageLoader;
import view.model.DeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static view.util.Constant.*;

public class SmallDeckBox extends Box<DeckOverview, SmallDeckBox.SmallDeckViewer, DeckActionListener> {
    public SmallDeckBox(int width, int height, JPanel parent, DeckActionListener deckActionListener) {
        super(width, height, parent, deckActionListener, SMALL_DECK_WIDTH, SMALL_DECK_HEIGHT, SMALL_DECK_SPACE);
    }

    @Override
    protected SmallDeckViewer createNew(DeckOverview deckOverview) {
        return new SmallDeckViewer(deckOverview);
    }


    public class SmallDeckViewer extends JPanel implements MouseListener {
        private final BufferedImage image;
        private final DeckOverview deckOverview;

        SmallDeckViewer(DeckOverview deckOverview) {
            this.deckOverview = deckOverview;
            this.image = ImageLoader.getInstance().getSmallDeck(deckOverview.getHeroName());
            this.setSize(Constant.SMALL_DECK_WIDTH, Constant.SMALL_DECK_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(image, 0, 0, this);
            g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
            g.setColor(Color.yellow);
            g.drawString("deck name:" + deckOverview.getDeckName(), 0, 20);
            g.drawString("hero name:" + deckOverview.getHeroName(), 0, 40);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null)
                    action.action(deckOverview.getDeckName());
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

