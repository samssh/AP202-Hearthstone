package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.CardOverview;

import javax.swing.*;

import static ir.sam.hearthstone.view.util.Constant.*;

public class CardBox extends Box<CardOverview, UnitViewer> {
    private final boolean distinct;

    public CardBox(int width, int height, JPanel parent, MyActionListener cardActionListener, boolean distinct) {
        super(width, height, parent, cardActionListener, CARD_WIDTH, CARD_HEIGHT, CARD_SPACE);
        this.distinct = distinct;
    }

    public CardBox(int width, int height, JPanel parent, MyActionListener action) {
        super(width, height, parent, action, SINGLE_CARD_WIDTH, CARD_HEIGHT, CARD_SPACE);
        this.distinct = false;
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

    @Override
    public void addModel(int index, CardOverview cardOverview, boolean animationOnNew) {
        if (distinct && models.contains(cardOverview)) {
            animationManger.start(() -> {
                CardOverview c = models.get(models.indexOf(cardOverview));
                c.setNumber(c.getNumber() + 1);
            });
        } else {
            super.addModel(index, cardOverview, animationOnNew);
        }
    }

    @Override
    public CardOverview removeModel(int index, boolean animationOnOld) {
        CardOverview cardOverview = models.get(index);
        if (distinct && cardOverview.getNumber()>1) {
            cardOverview.setNumber(cardOverview.getNumber()-1);
            return cardOverview.getClone();
        } else {
            return super.removeModel(index, animationOnOld);
        }
    }
}
