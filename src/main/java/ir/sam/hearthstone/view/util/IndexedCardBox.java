package ir.sam.hearthstone.view.util;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.OptionalInt;

public class IndexedCardBox extends CardBox {
    public IndexedCardBox(int width, int height, JPanel parent, MyActionListener cardActionListener) {
        super(width, height, parent, cardActionListener, false);
    }

    @Override
    protected UnitViewer createNew() {
        return new Viewer(parent, action);
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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (actionListener != null && unitOverview != null) {
                        OptionalInt optionalInt = getShowingIndex();
                        if (optionalInt.isPresent()) actionListener.action(optionalInt.getAsInt() + "");
                    }
                }
            }
        }

        private OptionalInt getShowingIndex() {
            for (int i = 0; i < IndexedCardBox.this.items.length; i++) {
                for (int j = 0; j < IndexedCardBox.this.items[i].length; j++) {
                    if (IndexedCardBox.this.items[i][j] == this) {
                        return OptionalInt.of(IndexedCardBox.this.begin + i + j * a);
                    }
                }
            }
            return OptionalInt.empty();
        }
    }

}
