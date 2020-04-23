package view.util;

import lombok.Setter;
import model.Card;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CardBox extends JPanel {
    private List<Card> cardList;
    private final List<CardViewer> cardViewers;
    private final int a, b, space = 10 , height, width;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    private final JPanel parent;
    private final boolean showPrice;
    @Setter
    private CardActionListener cardActionListener;

    public CardBox(int width, int height, JPanel parent,boolean showPrice) {
        this.a = width;
        this.b = height;
        this.parent = parent;
        this.showPrice = showPrice;
        this.width =width * (Constant.cardWidth + space);
        this.height =height * (Constant.cardHeight + space) + Constant.cardBoxButtonHeight + Constant.cardBoxLabelHeight;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        initializeTitle();
        initializeNext();
        initializePrevious();
        this.setOpaque(false);
        cardViewers = new ArrayList<>();
    }

    private void initializeTitle(){
        title = new JLabel();
        title.setBounds(0,0,width,Constant.cardBoxLabelHeight);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(false);
        title.setFocusable(false);
        title.setBorder(null);
    }

    private void initializeNext() {
        next = new JButton("next");
        next.setHorizontalAlignment(SwingConstants.RIGHT);
        next.setBounds(a * (Constant.cardWidth + space) - 70,
                b * (Constant.cardHeight + space) + 10 + Constant.cardBoxLabelHeight - space,
                70,Constant.cardBoxButtonHeight - 10);
        next.addActionListener(actionEvent->this.next());
        next.setOpaque(false);
        next.setFocusable(false);
        next.setContentAreaFilled(false);
        next.setFocusPainted(false);
        next.setBorderPainted(false);
    }

    private void initializePrevious() {
        previous = new JButton("previous");
        previous.setHorizontalAlignment(SwingConstants.LEFT);
        previous.setBounds(0,
                b * (Constant.cardHeight + space) + 10 + Constant.cardBoxLabelHeight - space,
                100,Constant.cardBoxButtonHeight - 10);
        previous.setFocusable(false);
        previous.setOpaque(false);
        previous.setContentAreaFilled(false);
        previous.setBorderPainted(false);
        previous.addActionListener(actionEvent -> this.previous());
        previous.setFocusPainted(false);
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
        end=0;
        next();
    }

    private void next() {
        cardViewers.clear();
        begin = end;
        for (int k = begin, t = 0; k < cardList.size(); t++, k++) {
            int i = t % a, j = t / a;
            if (j == b)
                break;
            Card card = cardList.get(k);
            CardViewer cardViewer;
            if (k + 1 < cardList.size() && card.equals(cardList.get(k + 1))) {
                k++;
                cardViewer = new CardViewer(card, 2, parent,showPrice);
            } else cardViewer = new CardViewer(card, 1, parent,showPrice);
            cardViewer.setLocation(i * (Constant.cardWidth + space),
                    j * (Constant.cardHeight + space) + Constant.cardBoxLabelHeight);
            cardViewer.setCardActionListener(cardActionListener);
            cardViewers.add(cardViewer);
            end = k + 1;
        }
        update();
    }

    private void previous() {
        cardViewers.clear();
        end = begin;
        for (int k = end -1 , t = a * b - 1; t >= 0; t--, k--) {
            int i = t % a, j = t / a;
            Card card = cardList.get(k);
            CardViewer cardViewer;
            if (k - 1 >=0 && card.equals(cardList.get(k - 1))) {
                k--;
                cardViewer = new CardViewer(card, 2, parent,showPrice);
            } else cardViewer = new CardViewer(card, 1, parent,showPrice);
            cardViewer.setLocation(i * (Constant.cardWidth + space),
                    j * (Constant.cardHeight + space) + Constant.cardBoxLabelHeight);
            cardViewers.add(cardViewer);
            begin = k;
        }
        update();
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

    public void setTitle(String title){
        this.title.setText(title);
    }
}
