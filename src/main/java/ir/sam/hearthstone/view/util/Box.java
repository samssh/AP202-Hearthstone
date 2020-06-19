package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.view.graphics_engine.*;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import ir.sam.hearthstone.view.model.Overview;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static ir.sam.hearthstone.view.util.Constant.*;

public abstract class Box<E extends Overview, T extends JPanel> extends JPanel {
    private List<E> models;
    private final LinkedList<E> showing;
    private final T[][] items;
    private final int a, b, height, width;
    private int begin, end;
    private JLabel title;
    private JButton next, previous;
    private final AnimationManger animationManger;
    protected final JPanel parent;
    protected final MyActionListener action;

    public Box(int width, int height, JPanel parent, MyActionListener action,
               int itemWidth, int itemHeight, int itemSpace) {
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
        this.items = createTArray(a, b);
        this.showing = new LinkedList<>();
        this.animationManger = new AnimationManger();
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                T t = createNew();
                t.setSize(itemWidth, itemHeight);
                t.setLocation(i * (itemWidth + itemSpace), j * (itemHeight + itemSpace) + BOX_LABEL_HEIGHT);
                items[i][j] = t;
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
        next.setBounds(width - BOX_BUTTON_WIDTH, height - BOX_BUTTON_HEIGHT
                , BOX_BUTTON_WIDTH, BOX_LABEL_HEIGHT);
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

    public void addModel(int index, E e) {
        models.add(index, e);
        if (begin <= index && index < end) {
            clear(index - begin);
            for (int k = index - begin, showingSize = showing.size(); k < showingSize && k + 1 < a * b; k++) {
                moveTo(k,k+1);
            }
            showing.add(index-begin, e);
            if (showing.size() > a * b) {
                animationManger.addPainter
                        (new TranslatorByTime(new SinglePictureScale(new OverviewPainter(showing.removeLast()),
                                true,ScaleOnCenter.ALL),items[a-1][b-1].getX(),items[a-1][b-1].getY()));
            } else end++;
            setButtons();
            animationManger.start(()->endAnimation(index - begin));
        } else {
            // todo tof
            throw new IllegalIndex(index);
        }
    }

    public void addModel(E e) {
        addModel(begin, e);
    }

    public void removeModel(int index){
        models.remove(index);
        if (begin <= index && index < end) {
            clear(index - begin);
            for (int k = index - begin + 1, showingSize = showing.size(); k < showingSize; k++) {
                moveTo(k,k-1);
            }
            showing.remove(index-begin);
            if (end <= models.size()) {
                E newTOShow = models.get(end);
                showing.addLast(newTOShow);
                animationManger.addPainter(new TranslatorByTime(new SinglePictureScale(
                        new OverviewPainter(newTOShow), false,ScaleOnCenter.ALL)
                        ,items[a-1][b-1].getX(),items[a-1][b-1].getY()));
            } else end++;
            setButtons();
            animationManger.start(()->endAnimation(index - begin));
        } else {
            // todo tof
            throw new IllegalIndex(index);
        }
    }

    private void moveTo(int k,int t){
        int originX = items[k % a][k / a].getX(), originY = items[k % a][k / a].getY();
        int destinationX = items[(t) % a][(t) / a].getX();
        int destinationY = items[(t) % a][(t) / a].getY();
        PaintByTime ove = new OverviewPainter(showing.get(k));
        animationManger.addPainter(new LinearMotion(originX, originY, destinationX, destinationY,
                ove, x -> x * x));
    }

    public void removeModel(E e){
        remove(showing.indexOf(e));
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
            end = k + 1;
            showing.addLast(models.get(k));
        }
        update();
    }

    private void previous() {
        end = begin;
        showing.clear();
        for (int k = end - 1; k >= end - a * b && k >= 0; k--) {
            begin = k;
            showing.addFirst(models.get(k));
        }
        update();
    }

    protected abstract T createNew();

    protected abstract void set(T t, E e);

    protected abstract T[][] createTArray(int i, int j);

    private void update() {
        this.clear(0);
        for (int i = 0, showingSize = showing.size(); i < showingSize; i++) {
            E e = showing.get(i);
            PaintByTime front = new OverviewPainter(e);
            animationManger.addPainter(new TranslatorByTime(
                    new SinglePictureScale(front, false, DoublePictureScale.RANDOM)
                    , items[i % a][i / a].getX(), items[i % a][i / a].getY()));
        }
        animationManger.start(this::endAnimation);
        setButtons();
    }

    private void setButtons(){
        this.remove(next);
        this.remove(previous);
        if (hasNext())
            this.add(next);
        if (hasPrevious())
            this.add(previous);
    }

    private void clear(int begin) {
        for (int k = begin; k < a * b; k++) {
            int i = k % a, j = k / a;
            set(items[i][j], null);
        }
    }

    private void endAnimation(int begin) {
        for (int i = begin, showingSize = showing.size(); i < showingSize; i++) {
            E e = showing.get(i);
            set(items[i % a][i / a], e);
        }
    }

    private void endAnimation() {
        endAnimation(0);
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
