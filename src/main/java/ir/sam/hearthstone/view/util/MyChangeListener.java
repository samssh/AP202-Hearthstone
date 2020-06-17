package ir.sam.hearthstone.view.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface MyChangeListener extends DocumentListener {
    void change(DocumentEvent e);

    default void insertUpdate(DocumentEvent e) {
        this.change(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e){
        this.change(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        this.change(e);
    }
}
