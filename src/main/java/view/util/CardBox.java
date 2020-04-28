package view.util;

import view.ImageLoader;
import view.model.CardOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static view.util.Constant.*;

public class CardBox extends JPanel {
    private List<CardOverview> cardList;
    private final List<CardViewer> cardViewers;
    private final int a, b, space = CARD_SPACE, height, width;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    private final JPanel parent;
    private final CardActionListener cardActionListener;

    public CardBox(int width, int height, JPanel parent, CardActionListener cardActionListener) {
        this.a = width;
        this.b = height;
        this.parent = parent;
        this.cardActionListener = cardActionListener;
        this.width = a * (CARD_WIDTH + space) - space;
        this.height = b * (CARD_HEIGHT + space) + BOX_BUTTON_HEIGHT + BOX_LABEL_HEIGHT;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        initializeTitle();
        initializeNext();
        initializePrevious();
        this.setOpaque(false);
        cardViewers = new ArrayList<>();
    }

    private void initializeTitle() {
        title = new JLabel();
        title.setBounds(0, 0, width, BOX_LABEL_HEIGHT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(false);
        title.setFocusable(false);
        title.setBorder(null);
    }

    private void initializeNext() {
        next = new JButton("next");
        next.setHorizontalAlignment(SwingConstants.RIGHT);
        next.setBounds(width - BOX_BUTTON_WIDTH, height - BOX_BUTTON_HEIGHT, BOX_BUTTON_WIDTH, BOX_LABEL_HEIGHT);
        next.addActionListener(actionEvent -> this.next());
        next.setOpaque(false);
        next.setFocusable(false);
        next.setContentAreaFilled(false);
        next.setFocusPainted(false);
        next.setBorderPainted(false);
    }

    private void initializePrevious() {
        previous = new JButton("previous");
        previous.setHorizontalAlignment(SwingConstants.LEFT);
        previous.setBounds(0, height - BOX_BUTTON_HEIGHT, BOX_BUTTON_WIDTH, BOX_LABEL_HEIGHT);
        previous.setFocusable(false);
        previous.setOpaque(false);
        previous.setContentAreaFilled(false);
        previous.setBorderPainted(false);
        previous.addActionListener(actionEvent -> this.previous());
        previous.setFocusPainted(false);
    }

    public void setCardList(List<CardOverview> cardList) {
        this.cardList = cardList;
        end = 0;
        next();
    }

    private void next() {
        cardViewers.clear();
        begin = end;
        for (int k = begin; k < cardList.size() && k < begin + a * b; k++) {
            int i = (k - begin) % a, j = (k - begin) / a;
            f(cardViewers, k, i, j);
            end = k + 1;
        }
        update();
    }

    private void previous() {
        cardViewers.clear();
        end = begin;
        for (int k = end - 1; k >= end - a * b && k >= 0; k--) {
            int i = (k - end + a * b) % a, j = (k - end + a * b) / a;
            f(cardViewers, k, i, j);
            begin = k;
        }
        update();
    }

    private void f(List<CardViewer> cardViewers, int k, int i, int j) {
        CardOverview cardOverview = cardList.get(k);
        CardViewer cardViewer;
        cardViewer = new CardViewer(cardOverview);
        cardViewer.setLocation(i * (CARD_WIDTH + space), j * (CARD_HEIGHT + space) + BOX_LABEL_HEIGHT);
        cardViewers.add(cardViewer);
    }

    private void update() {
        this.removeAll();
        for (CardViewer cardViewer : cardViewers) {
            this.add(cardViewer);
        }
        if (hasNext())
            this.add(next);
        if (hasPrevious())
            this.add(previous);
        this.add(title);
    }

    private boolean hasNext() {
        return end != cardList.size();
    }

    private boolean hasPrevious() {
        return begin != 0;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


    public class CardViewer extends JPanel implements MouseListener {
        private final BufferedImage small, big;
        private final CardOverview cardOverview;


        CardViewer(CardOverview cardOverview) {
            this.cardOverview = cardOverview;
            setToolTipText("class of card: " + cardOverview.getClassOfCard());
            if (cardOverview.getColorType() == 0) {
                small = ImageLoader.getInstance().getSmallCard(cardOverview.getName());
                big = ImageLoader.getInstance().getBigCard(cardOverview.getName());
            } else if (cardOverview.getColorType() == 1) {
                small = ImageLoader.getInstance().getSmallGrayCard(cardOverview.getName());
                big = ImageLoader.getInstance().getBigGrayCard(cardOverview.getName());
            } else {
                small = null;
                big = null;
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
                if (big != null) {
                    JOptionPane.showMessageDialog(parent, null,
                            "card information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(big));
                }
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                cardActionListener.action(cardOverview.getName());
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
