package view.util;

import util.ImageLoader;
import view.model.CardOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static view.util.Constant.*;

public class CardBox extends Box<CardOverview, CardBox.CardViewer, CardActionListener> {
    public CardBox(int width, int height, JPanel parent, CardActionListener cardActionListener) {
        super(width, height, parent, cardActionListener, CARD_WIDTH, CARD_HEIGHT, CARD_SPACE);
    }

    @Override
    protected CardViewer createNew(CardOverview cardOverview) {
        return new CardViewer(cardOverview);
    }


    public class CardViewer extends JPanel implements MouseListener {
        private final BufferedImage small, big;
        private final CardOverview cardOverview;


        CardViewer(CardOverview cardOverview) {
            this.cardOverview = cardOverview;
            setToolTipText("class of card: " + cardOverview.getClassOfCard());
            if (cardOverview.getNumber() > 0) {
                small = ImageLoader.getInstance().getSmallCard(cardOverview.getName());
                big = ImageLoader.getInstance().getBigCard(cardOverview.getName());
            } else {
                small = ImageLoader.getInstance().getSmallGrayCard(cardOverview.getName());
                big = ImageLoader.getInstance().getBigGrayCard(cardOverview.getName());
            }
            this.setSize(Constant.CARD_WIDTH, Constant.CARD_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (cardOverview.getNumber() == 2)
                g.drawImage(small, 15, 0, this);
            g.drawImage(small, 0, 0, this);
            if (cardOverview.isShowPrice()) {
                g.setFont(g.getFont().deriveFont(17.0F).deriveFont(Font.BOLD));
                g.setColor(Color.RED);
                g.drawString("price: " + cardOverview.getPrice(), 35 * this.getWidth() / 135, 155 * this.getHeight() / 170);
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                JOptionPane.showMessageDialog(parent, null,
                        "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(big));
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (action != null) {
                    action.action(cardOverview.getName());
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

}
