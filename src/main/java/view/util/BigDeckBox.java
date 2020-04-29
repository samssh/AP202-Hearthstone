package view.util;

import util.ImageLoader;
import view.model.DeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static view.util.Constant.*;

public class BigDeckBox extends Box<DeckOverview, BigDeckBox.SmallDeckViewer, DeckActionListener> {
    public BigDeckBox(int width, int height, JPanel parent, DeckActionListener deckActionListener) {
        super(width, height, parent, deckActionListener, BIG_DECK_WIDTH, BIG_DECK_HEIGHT, BIG_DECK_SPACE);
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
            this.image = ImageLoader.getInstance().getBigDeck(deckOverview.getHeroName());
            this.setSize(Constant.BIG_DECK_WIDTH, Constant.BIG_DECK_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
            // todo draw information
            g.setFont(new Font("War Priest Expanded", Font.PLAIN, 20));
            g.setColor(Color.yellow);
            g.drawString("deck name:" + deckOverview.getDeckName(), 0, 40);
            g.drawString("hero name:" + deckOverview.getHeroName(), 0, 80);
            String s;
            if (deckOverview.getWinRate() != -1) s = new DecimalFormat("#.##").format(deckOverview.getWinRate());
            else s = "--";
            g.drawString("wins:" + deckOverview.getWins() + " games:" + deckOverview.getGames() + " winRate:" + s, 0, 120);
            String p;
            if (deckOverview.getManaAverage() != -1) {
                p = new DecimalFormat("#.##").format(deckOverview.getManaAverage());
                g.drawString("mana average:" + p, 0, 160);
                g.drawString("MVC:" + deckOverview.getCardName(), 0, 200);
            } else g.drawString("deck empty", 0, 160);

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
