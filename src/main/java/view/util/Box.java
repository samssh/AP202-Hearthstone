package view.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static view.util.Constant.*;

public abstract class Box<E, T extends JPanel> extends JPanel {
    private List<E> models;
    private final List<T> items;
    private final int a, b, height, width;
    private final int itemWidth, itemHeight, itemSpace;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    protected final JPanel parent;
    protected final MyActionListener action;

    public Box(int width, int height, JPanel parent, MyActionListener action, int itemWidth, int itemHeight, int itemSpace) {
        this.a = width;
        this.b = height;
        this.parent = parent;
        this.action = action;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.itemSpace = itemSpace;
        this.width = a * (itemWidth + itemSpace) - itemSpace;
        this.height = b * (itemHeight + itemSpace) + BOX_BUTTON_HEIGHT + BOX_LABEL_HEIGHT;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        this.initializeTitle();
        this.initializeNext();
        this.initializePrevious();
        this.setOpaque(false);
        this.items = new ArrayList<>();
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
        next.setBorderPainted(false);
        next.setContentAreaFilled(false);
        next.addActionListener(actionEvent -> this.next());
        next.setOpaque(false);
        next.setFocusable(false);
        next.setHorizontalAlignment(SwingConstants.RIGHT);
        next.setBounds(width - BOX_BUTTON_WIDTH, height - BOX_BUTTON_HEIGHT, BOX_BUTTON_WIDTH, BOX_LABEL_HEIGHT);
        next.setFocusPainted(false);

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

    public void setModels(List<E> models) {
        this.models = models;
        end = 0;
        next();
    }

    private void next() {
        items.clear();
        begin = end;
        for (int k = begin; k < models.size() && k < begin + a * b; k++) {
            int i = (k - begin) % a, j = (k - begin) / a;
            end = k + 1;
            f(items, k, i, j);
        }
        update();
    }

    private void previous() {
        end = begin;
        items.clear();
        for (int k = end - 1; k >= end - a * b && k >= 0; k--) {
            int i = (k - end + a * b) % a, j = (k - end + a * b) / a;
            begin = k;
            f(items, k, i, j);
        }
        update();
    }

    private void f(List<T> items, int k, int i, int j) {
        E e = models.get(k);
        T t = createNew(e);
        t.setLocation(i * (itemWidth + itemSpace), j * (itemHeight + itemSpace) + BOX_LABEL_HEIGHT);
        items.add(t);
    }

    abstract T createNew(E e);

    private void update() {
        this.removeAll();
        for (T t : items) {
            this.add(t);
        }
        if (hasNext())
            this.add(next);
        if (hasPrevious())
            this.add(previous);
        this.add(title);
    }

    private boolean hasNext() {
        return end != models.size();
    }

    private boolean hasPrevious() {
        return begin != 0;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
