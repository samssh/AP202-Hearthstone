package view.util;

import util.ImageLoader;
import view.model.DeckOverview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static view.util.Constant.*;

public class DeckBox extends JPanel {
    private List<DeckOverview> deckList;
    private final List<DeckViewer> deckViewers;
    private final int a, b, space = DECK_SPACE, height, width;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    private final JPanel parent;
    private final DeckActionListener deckActionListener;

    public DeckBox(int width, int height, JPanel parent, DeckActionListener deckActionListener) {
        this.a = width;
        this.b = height;
        this.parent = parent;
        this.deckActionListener = deckActionListener;
        this.width = a * (DECK_WIDTH + space) - space;
        this.height = b * (DECK_HEIGHT + space) + BOX_BUTTON_HEIGHT + BOX_LABEL_HEIGHT;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        this.initializeTitle();
        this.initializeNext();
        this.initializePrevious();
        this.setOpaque(false);
        this.deckViewers = new ArrayList<>();
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
        next.setFocusPainted(false);
        next.setBorderPainted(false);
        next.setContentAreaFilled(false);
        next.addActionListener(actionEvent -> this.next());
        next.setOpaque(false);
        next.setFocusable(false);
    }

    private void initializePrevious() {
        previous = new JButton("previous");
        previous.setHorizontalAlignment(SwingConstants.LEFT);
        previous.setBounds(0, height - BOX_BUTTON_HEIGHT, BOX_BUTTON_WIDTH, BOX_LABEL_HEIGHT);
        previous.setFocusable(false);
        previous.setContentAreaFilled(false);
        previous.setBorderPainted(false);
        previous.setOpaque(false);
        previous.addActionListener(actionEvent -> this.previous());
        previous.setFocusPainted(false);
    }

    public void setDeckList(List<DeckOverview> deckList) {
        this.deckList = deckList;
        end = 0;
        next();
    }

    private void next() {
        deckViewers.clear();
        begin = end;
        for (int k = begin; k < deckList.size() && k < begin + a * b; k++) {
            int i = (k - begin) % a, j = (k - begin) / a;
            end = k + 1;
            f(deckViewers, k, i, j);
        }
        update();
    }

    private void previous() {
        end = begin;
        deckViewers.clear();
        for (int k = end - 1; k >= end - a * b && k >= 0; k--) {
            int i = (k - end + a * b) % a, j = (k - end + a * b) / a;
            begin = k;
            f(deckViewers, k, i, j);
        }
        update();
    }

    private void f(List<DeckViewer> deckViewers, int k, int i, int j) {
        DeckOverview deckOverview = deckList.get(k);
        DeckViewer deckViewer;
        deckViewer = new DeckViewer(deckOverview);
        deckViewer.setLocation(i * (DECK_WIDTH + space), j * (DECK_HEIGHT + space) + BOX_LABEL_HEIGHT);
        deckViewers.add(deckViewer);
    }

    private void update() {
        this.removeAll();
        for (DeckViewer deckViewer : deckViewers) {
            this.add(deckViewer);
        }
        if (hasNext())
            this.add(next);
        if (hasPrevious())
            this.add(previous);
        this.add(title);
    }

    private boolean hasNext() {
        return end != deckList.size();
    }

    private boolean hasPrevious() {
        return begin != 0;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


    public class DeckViewer extends JPanel implements MouseListener {
        private final BufferedImage image;
        private final DeckOverview deckOverview;

        DeckViewer(DeckOverview deckOverview) {
            this.deckOverview = deckOverview;
            this.image = ImageLoader.getInstance().getDeck(deckOverview.getHeroName());
            this.setSize(Constant.DECK_WIDTH, Constant.DECK_HEIGHT);
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
            g.drawString("deck name:"+deckOverview.getDeckName(),0,40);
            g.drawString("hero name:"+deckOverview.getHeroName(),0,80);
            String s;
            if (deckOverview.getWinRate()!=-1) s = new DecimalFormat("#.##").format(deckOverview.getWinRate());
            else s="--";
            g.drawString("wins:"+deckOverview.getWins()+" games:"+deckOverview.getGames()+" winRate:" + s,0,120);
            String p;
            if (deckOverview.getManaAverage()!=-1){
                p = new DecimalFormat("#.##").format(deckOverview.getManaAverage());
                g.drawString("mana average:"+p,0,160);
                g.drawString("MVC:"+deckOverview.getCardName(),0,200);
            }
            else g.drawString("deck empty",0,160);

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (DeckBox.this.deckActionListener != null)
                    DeckBox.this.deckActionListener.action(deckOverview.getDeckName());
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
