package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.CardOverview;

import javax.swing.*;

import static ir.sam.hearthstone.view.util.Constant.*;

public class CardBox extends Box<CardOverview, UnitViewer> {
    public CardBox(int width, int height, JPanel parent, MyActionListener cardActionListener) {
        super(width, height, parent, cardActionListener, CARD_WIDTH, CARD_HEIGHT, CARD_SPACE);
    }

    @Override
    protected UnitViewer createNew() {
        return new UnitViewer(parent, action);
    }

    @Override
    protected void set(UnitViewer unitViewer, CardOverview cardOverview) {
        unitViewer.setUnitOverview(cardOverview);
    }

    @Override
    protected UnitViewer[][] createTArray(int i, int j) {
        return new UnitViewer[i][j];
    }


}
