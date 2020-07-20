package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.model.MinionOverview;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.OptionalInt;

import static ir.sam.hearthstone.view.util.Constant.*;

public class MinionBox extends Box<MinionOverview, MinionBox.Viewer> {

    public MinionBox(int width, int height, JPanel parent, MyActionListener action) {
        super(width, height, parent, action, Constant.MINION_WIDTH, Constant.MINION_HEIGHT, Constant.MINION_SPACE);
        this.height = b * (MINION_HEIGHT + MINION_SPACE) - MINION_SPACE;
        this.setSize(this.width, this.height);
        this.remove(title);
    }

    @Override
    protected void initializeItems(int itemWidth, int itemHeight, int itemSpace) {
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                MinionBox.Viewer viewer = createNew();
                viewer.setSize(itemWidth, itemHeight);
                viewer.setLocation(i * (itemWidth + itemSpace), j * (itemHeight + itemSpace));
                items[i][j] = viewer;
                this.add(viewer);
            }
        }
    }

    @Override
    protected Viewer createNew() {
        return new Viewer(parent, action);
    }

    @Override
    protected void set(Viewer viewer, MinionOverview minionOverview) {
        viewer.setUnitOverview(minionOverview);
    }

    @Override
    protected Viewer[][] createTArray(int i, int j) {
        return new Viewer[i][j];
    }

    class Viewer extends UnitViewer {
        public Viewer(JPanel parent, MyActionListener actionListener) {
            super(parent, actionListener);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (unitOverview != null) {
                if (e.getButton() == MouseEvent.BUTTON3) JOptionPane.showMessageDialog(this.parent, null,
                        "information", JOptionPane.INFORMATION_MESSAGE
                        , new ImageIcon(unitOverview.getBigImage()));
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (actionListener != null /*&& unitOverview != null*/) {
                    OptionalInt optionalInt = getShowingIndex();
                    if (optionalInt.isPresent()) {
                        int x = 0;
                        if (e.getX() > this.getWidth() / 2)
                            x = 1;
                        actionListener.action(optionalInt.getAsInt() + "," + x);
                    }
                }
            }
        }

        private OptionalInt getShowingIndex() {
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    if (items[i][j] == this) {
                        return OptionalInt.of(begin + i + j * a);
                    }
                }
            }
            return OptionalInt.empty();
        }
    }
}
