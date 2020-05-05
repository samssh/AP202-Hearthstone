package view.util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MyJTextField extends JTextField implements DocumentListener {
    private MyChangeListener myChangeListener;
    @Override
    public void insertUpdate(DocumentEvent e) {
        myChangeListener.change(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        myChangeListener.change(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        myChangeListener.change(e);
    }

    public void setChangeListener(MyChangeListener myChangeListener){
        this.myChangeListener = myChangeListener;
        this.getDocument().addDocumentListener(this);
    }
}
