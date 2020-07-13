package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.MinionOverview;

import javax.swing.*;

public class MinionBox extends Box<MinionOverview, UnitViewer>{

    public MinionBox(int width, int height, JPanel parent, MyActionListener action) {
        super(width, height, parent, action, Constant.MINION_WIDTH, Constant.MINION_HEIGHT, Constant.MINION_SPACE);
    }

    @Override
    protected UnitViewer createNew() {
        return new UnitViewer(parent,action);
    }

    @Override
    protected void set(UnitViewer unitViewer, MinionOverview minionOverview) {
        unitViewer.setUnitOverview(minionOverview);
    }

    @Override
    protected UnitViewer[][] createTArray(int i, int j) {
        return new UnitViewer[i][j];
    }
}
