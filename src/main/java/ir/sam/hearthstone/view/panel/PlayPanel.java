package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.Actions.PlayAction;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.util.*;
import ir.sam.hearthstone.view.util.Box;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static ir.sam.hearthstone.view.util.Constant.*;

public class PlayPanel extends JPanel {
    static BufferedImage manaImage;

    static {
        try {
            manaImage = ImageIO.read(new File("C:\\Users\\HP\\Downloads\\mana (1).png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CardBox[] hand;
    private MinionBox[] ground;
    private UnitViewer[] hero, weapon, heroPower;
    private JButton exit, next;
    private JTextArea eventLog;
    private JScrollPane scrollPane;
    private final AnimationManger animationManger;
    private final int[] mana = new int[2];
    private final PlayAction playAction;
    private final BufferedImage backgound;
    private int x, y, width, height;
    private Integer[] manaX, manaY;
    private Integer manaSpace;
    private Integer[] handX, handY, handWidth, handHeight;
    private Integer[] groundX, groundY, groundWidth, groundHeight;
    private Integer[] heroX, heroY, heroSpace;
    private int nextX, nextY, nextWidth, nextHeight;
    private int exitX, exitY, exitWidth, exitHeight;
    private int eventLogX, eventLogY, eventLogWidth, eventLogHeight;

    public PlayPanel(PlayAction playAction) {
        setLayout(null);
        this.playAction = playAction;
        this.backgound = ImageLoader.getInstance().getBackground("play");
        config();
        initialize();
        this.setBounds(x, y, width, height);
        this.add(hand[0]);
        this.add(ground[0]);
        this.add(hero[0]);
        this.add(weapon[0]);
        this.add(heroPower[0]);
        this.add(hand[1]);
        this.add(ground[1]);
        this.add(hero[1]);
        this.add(weapon[1]);
        this.add(heroPower[1]);
        mana[0] = 4;
        mana[1] = 10;
        this.add(scrollPane);
        this.add(exit);
        this.add(next);
        animationManger = new AnimationManger();
    }

    private void initialize() {
        initializeHands();
        initializeGrounds();
        initializeHero();
        initializeWeapon();
        initializeHeroPower();
        initializeExit();
        initializeNext();
        initializeEventLog();
    }

    private void initializeBox(Box<?, ?> box, String title, int x, int y) {
        box.setTitle(title);
        box.setLocation(x, y);
    }

    private void initializeHands() {
        hand = new CardBox[2];
        hand[0] = new IndexedCardBox(handWidth[0], handHeight[0], this, playAction::playCard);
        hand[1] = new IndexedCardBox(handWidth[1], handHeight[1], this, playAction::playCard);
        initializeBox(hand[0], "Your hand card", handX[0], handY[0]);
        initializeBox(hand[1], "Opponent hand card", handX[1], handY[1]);
    }

    private void initializeGrounds() {
        ground = new MinionBox[2];
        ground[0] = new MinionBox(groundWidth[0], groundHeight[0], this, null);
        ground[1] = new MinionBox(groundWidth[1], groundHeight[1], this, null);
        initializeBox(ground[0], "your battleground minions", groundX[0], groundY[0]);
        initializeBox(ground[1], "Opponent battleground minions", groundX[1], groundY[1]);
    }

    private void initializeViewer(UnitViewer unitViewer, int x, int y, int width, int height) {
        unitViewer.setBounds(x, y, width, height);
    }

    private void initializeHero() {
        hero = new UnitViewer[2];
        hero[0] = new UnitViewer(this);
        initializeViewer(hero[0], heroX[0], heroY[0], HERO_WIDTH, HERO_HEIGHT);
        hero[1] = new UnitViewer(this);
        initializeViewer(hero[1], heroX[1], heroY[1], HERO_WIDTH, HERO_HEIGHT);
    }

    private void initializeWeapon() {
        weapon = new UnitViewer[2];
        weapon[0] = new UnitViewer(this);
        int x = heroX[0] - heroSpace[0] - CARD_WIDTH;
        initializeViewer(weapon[0], x, heroY[0], CARD_WIDTH, CARD_HEIGHT);
        weapon[1] = new UnitViewer(this);
        x = heroX[1] - heroSpace[1] - CARD_WIDTH;
        initializeViewer(weapon[1], x, heroY[1], CARD_WIDTH, CARD_HEIGHT);
    }

    private void initializeHeroPower() {
        heroPower = new UnitViewer[2];
        heroPower[0] = new UnitViewer(this);
        int x = heroX[0] + heroSpace[0] + HERO_WIDTH;
        initializeViewer(heroPower[0], x, heroY[0], HERO_POWER_WIDTH, HERO_POWER_HEIGHT);
        heroPower[1] = new UnitViewer(this);
        x = heroX[1] + heroSpace[1] + HERO_WIDTH;
        initializeViewer(heroPower[1], x, heroY[1], HERO_POWER_WIDTH, HERO_POWER_HEIGHT);
    }

    private void initializeExit() {
        exit = new JButton("<html>E<br>n<br>d<br> <br>G<br>a<br>m<br>e</html>");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> playAction.exit());
        Constant.makeTransparent(exit);
        exit.setBackground(new Color(240, 240, 240, 100));
    }

    private void initializeNext() {
        next = new JButton("<html>N<br>e<br>x<br>t<br> <br>T<br>u<br>r<br>n</html>");
        next.setHorizontalAlignment(SwingConstants.CENTER);
        next.setBounds(nextX, nextY, nextWidth, nextHeight);
        next.addActionListener(e -> playAction.endTurn());
        Constant.makeTransparent(next);
        next.setBackground(new Color(240, 240, 240, 100));
    }

    private void initializeEventLog() {
        eventLog = new JTextArea();
        scrollPane = new JScrollPane(eventLog);
        scrollPane.setBounds(eventLogX, eventLogY, eventLogWidth, eventLogHeight);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBorder(null);
        scrollPane.setBorder(null);
        eventLog.setBorder(null);
        eventLog.setOpaque(false);
        eventLog.setForeground(Color.WHITE);
        eventLog.setEditable(false);
        scrollPane.getViewport().setOpaque(false);
    }

    public void setDetails(List<PlayDetails.Event> events, String eventLog, int[] mana) {
        animationManger.clear();
        events.forEach(this::executeEvent);
        animationManger.start();
        this.eventLog.setText(eventLog);
        this.mana[0] = mana[0];
        this.mana[1] = mana[1];
    }

    private void executeEvent(PlayDetails.Event event) {
        switch (event.getType()) {
            case SET_HERO:
                hero[event.getSide()].setUnitOverviewAnimated(event.getOverview());
                break;
            case SET_HERO_POWER:
                heroPower[event.getSide()].setUnitOverviewAnimated(event.getOverview());
                break;
            case PLAY_WEAPON:
                Point point = hand[event.getSide()].getPosition(event.getIndex());
                point.translate(hand[event.getSide()].getX(), hand[event.getSide()].getY());
                PaintByTime old = new OverviewPainter(hand[event.getSide()].removeModel(event.getIndex()));
                PaintByTime neW = new OverviewPainter(event.getOverview());
                PaintByTime painter = new DoublePictureScale(neW, old, ScaleOnCenter.ALL);
                painter = new LinearMotion(point.x, point.y, weapon[event.getSide()].getX()
                        , weapon[event.getSide()].getY(), painter, x -> Math.pow(x, 1.2));
                animationManger.addPainter(painter);
                animationManger.addEndAnimationAction(
                        () -> weapon[event.getSide()].setUnitOverviewAnimated(event.getOverview()));
                break;
            case ADD_TO_GROUND:

                break;
            case ADD_TO_HAND:
                hand[event.getSide()].addModel((CardOverview) event.getOverview(),true);
                break;
            case MOVE_FROM_HAND_TO_GROUND:
                break;
            case MOVE_FROM_GROUND_TO_HAND:
                break;
            case ATTACK_MINION_TO_HERO:
                break;
            case ATTACK_MINION_TO_MINION:
                break;
            case ATTACK_HERO_TO_MINION:
                break;
            case ATTACK_HERO_TO_HERO:
                break;
            case PLAY_SPELL:
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgound, 0, 0, null);
        animationManger.paint((Graphics2D) g);
        g.setColor(Color.WHITE);
        for (int i = 0; i < this.mana[0]; i++) {
            g.drawImage(manaImage, manaX[0], manaY[0] + i * (manaImage.getHeight() + manaSpace), null);
        }
        for (int i = 0; i < mana[1]; i++) {
            g.drawImage(manaImage, manaX[1], manaY[1] - i * (manaImage.getHeight() + manaSpace), null);
        }
    }

    private void config() {
        Config config = ConfigFactory.getInstance().getConfig("PLAY_PANEL");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");
        handX = config.getPropertyArray(Integer.class, "handX");
        handY = config.getPropertyArray(Integer.class, "handY");
        handWidth = config.getPropertyArray(Integer.class, "handWidth");
        handHeight = config.getPropertyArray(Integer.class, "handHeight");
        groundX = config.getPropertyArray(Integer.class, "groundX");
        groundY = config.getPropertyArray(Integer.class, "groundY");
        groundWidth = config.getPropertyArray(Integer.class, "groundWidth");
        groundHeight = config.getPropertyArray(Integer.class, "groundHeight");
        heroX = config.getPropertyArray(Integer.class, "heroX");
        heroY = config.getPropertyArray(Integer.class, "heroY");
        heroSpace = config.getPropertyArray(Integer.class, "heroSpace");
        nextX = config.getProperty(Integer.class, "nextX");
        nextY = config.getProperty(Integer.class, "nextY");
        nextWidth = config.getProperty(Integer.class, "nextWidth");
        nextHeight = config.getProperty(Integer.class, "nextHeight");
        eventLogX = config.getProperty(Integer.class, "eventLogX");
        eventLogY = config.getProperty(Integer.class, "eventLogY");
        eventLogWidth = config.getProperty(Integer.class, "eventLogWidth");
        eventLogHeight = config.getProperty(Integer.class, "eventLogHeight");
        exitX = config.getProperty(Integer.class, "exitX");
        exitY = config.getProperty(Integer.class, "exitY");
        exitWidth = config.getProperty(Integer.class, "exitWidth");
        exitHeight = config.getProperty(Integer.class, "exitHeight");
        manaX = config.getPropertyArray(Integer.class, "manaX");
        manaY = config.getPropertyArray(Integer.class, "manaY");
        manaSpace = config.getProperty(Integer.class, "manaSpace");
    }
}
