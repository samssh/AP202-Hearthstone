package view.util;

import util.ImageLoader;
import view.model.CardOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

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
        private final BufferedImage big;
        private final CardOverview cardOverview;


        CardViewer(CardOverview cardOverview) {
            this.cardOverview = cardOverview;
            setToolTipText("class of card: " + cardOverview.getClassOfCard());
            if (cardOverview.getNumber() > 0) {
                big = ImageLoader.getInstance().getBigCard(cardOverview.getName());
            } else {
                big = ImageLoader.getInstance().getBigGrayCard(cardOverview.getName());
            }
            this.setSize(Constant.CARD_WIDTH, Constant.CARD_HEIGHT);
            this.setOpaque(false);
            this.addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            cardOverview.paint(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                ColorModel cm =big.getColorModel();
                BufferedImage image = new BufferedImage(cm,big.copyData(null),cm.isAlphaPremultiplied(),null);
                Graphics g = image.createGraphics();
                g.setClip(0,0,image.getWidth(),image.getHeight());
                cardOverview.paintBig(g);
                JOptionPane.showMessageDialog(parent, null,
                        "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));
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
