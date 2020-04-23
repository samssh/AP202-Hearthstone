package view.util;

import lombok.Setter;
import model.Card;
import view.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CardViewer extends JPanel {
    private final Card card;
    private final BufferedImage small, big;
    private final int number;
    private final JPanel parent;
    private final boolean showPrice;
    @Setter
    private CardActionListener cardActionListener;

    CardViewer(Card card, int number, JPanel parent, boolean showPrice) {
        this.card = card;
        this.number = number;
        this.parent = parent;
        this.showPrice = showPrice;
//        this.cardActionListener = cardActionListener;
        small = ImageLoader.getInstance().getSmallCard(card.getName());
        big = ImageLoader.getInstance().getBigCard(card.getName());
        this.setSize(Constant.cardWidth, Constant.cardHeight);
        this.setOpaque(false);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JOptionPane.showMessageDialog(CardViewer.this.parent, null,
                            "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(big));
                }
                if (e.getButton() == MouseEvent.BUTTON1){
                    cardActionListener.action(card);
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
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (number == 2)
            g.drawImage(small, 15, 0, this);
        g.drawImage(small, 0, 0, this);
        if (showPrice) {
            g.setFont(g.getFont().deriveFont(17.0F).deriveFont(Font.BOLD));
            g.setColor(Color.RED);
            g.drawString("price: " + card.getPrice(), 35*this.getWidth()/135, 155*this.getHeight()/170);
        }

    }
}
