package view.util;

import view.model.CardOverview;

import javax.swing.*;

import static view.util.Constant.*;

public class CardBox extends Box<CardOverview, UnitViewer> {
    public CardBox(int width, int height, JPanel parent, MyActionListener cardActionListener) {
        super(width, height, parent, cardActionListener, CARD_WIDTH, CARD_HEIGHT, CARD_SPACE);
    }

    @Override
    protected UnitViewer createNew(CardOverview cardOverview) {
        return new UnitViewer(cardOverview, parent, action);
    }


}
