package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import ir.sam.hearthstone.view.graphics_engine.effects.TranslatorByTime;
import ir.sam.hearthstone.view.model.Overview;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ir.sam.hearthstone.view.util.Constant.*;

public abstract class Box<E extends Overview, T extends JPanel> extends JPanel {
    private List<E> models;
    private final LinkedList<E> showing;
    private final List<T> items;
    private final int a, b, height, width;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    private final AnimationManger animationManger;
    protected final JPanel parent;
    protected final MyActionListener action;

    public Box(int width, int height, JPanel parent, MyActionListener action, int itemWidth, int itemHeight, int itemSpace) {
        this.a = width;
        this.b = height;
        this.parent = parent;
        this.action = action;
        this.width = a * (itemWidth + itemSpace) - itemSpace;
        this.height = b * (itemHeight + itemSpace) + BOX_BUTTON_HEIGHT + BOX_LABEL_HEIGHT;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        this.initializeTitle();
        this.initializeNext();
        this.initializePrevious();
        this.setOpaque(false);
        this.items = new ArrayList<>();
        this.showing = new LinkedList<>();
        this.animationManger = new AnimationManger();
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                T t = createNew();
                t.setSize(itemWidth,itemHeight);
                t.setLocation(i * (itemWidth + itemSpace), j * (itemHeight + itemSpace) + BOX_LABEL_HEIGHT);
                items.add(t);
                this.add(t);
            }
        }
        this.add(title);
    }

    private void initializeTitle() {
        title = new JLabel();
        title.setBounds(0, 0, width, BOX_LABEL_HEIGHT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(false);
        title.setFocusable(false);
        title.setBorder(null);
        title.setFont(title.getFont().deriveFont(17F));
        title.setForeground(Color.WHITE);
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
        next.setForeground(Color.WHITE);

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
        previous.setForeground(Color.WHITE);
    }

    public void setModels(List<E> models) {
        this.models = models;
        end = 0;
        next();
    }

    private void next() {
        showing.clear();
        begin = end;
        for (int k = begin; k < models.size() && k < begin + a * b; k++) {
            int i = (k - begin) % a, j = (k - begin) / a;
            end = k + 1;
            showing.addLast(models.get(k));
        }
        update();
    }

    private void previous() {
        end = begin;
        showing.clear();
        for (int k = end - 1; k >= end - a * b && k >= 0; k--) {
            int i = (k - end + a * b) % a, j = (k - end + a * b) / a;
            begin = k;
            showing.addFirst(models.get(k));
        }
        update();
    }

    protected abstract T createNew();

    protected abstract void set(T t,E e);

    private void update() {
        this.clear();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File("./src/main/resources/images/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0, showingSize = showing.size(); i < showingSize; i++) {
            T t=items.get(i);
            E e=showing.get(i);
            PaintByTime back = new SimplePainter(bufferedImage);
            PaintByTime front = new OverviewPainter(e);
            animationManger.addPainter(new TranslatorByTime(new FlipImage(front,back,FlipImage.ALL),t.getX(),t.getY()));
        }
        animationManger.start(this::endAnimation);
        this.remove(next);
        this.remove(previous);
        if (hasNext())
            this.add(next);
        if (hasPrevious())
            this.add(previous);
    }

    private void clear(){
        for (T t:items) {
            set(t,null);
        }
    }

    private void endAnimation(){
        for (int i = 0, showingSize = showing.size(); i < showingSize; i++) {
            set(items.get(i),showing.get(i));
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        animationManger.paint((Graphics2D) g);
    }
}
