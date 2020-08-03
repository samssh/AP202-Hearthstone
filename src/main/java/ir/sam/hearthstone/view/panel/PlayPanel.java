package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.actions.PlayAction;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.util.Box;
import ir.sam.hearthstone.view.util.*;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static ir.sam.hearthstone.view.util.Constant.*;

public class PlayPanel extends JPanel {
    private final BufferedImage manaImage;
    @Getter
    private CardBox[] hand;
    @Getter
    private MinionBox[] ground;
    @Getter
    private UnitViewer[] hero, weapon, heroPower;
    private JButton exit, next;
    private JTextArea eventLog;
    private JScrollPane scrollPane;
    @Getter
    private final AnimationManger animationManger;
    private final int[] mana = new int[2];
    private final PlayAction playAction;
    private final BufferedImage background;
    private JProgressBar progressBar;
    private final PlayEventExecutor executor;
    private long time;
    private int x, y, width, height;
    private Integer[] manaX, manaY;
    private Integer manaSpace;
    @Getter
    private Integer[] handX, handY, handWidth, handHeight;
    @Getter
    private Integer[] groundX, groundY, groundWidth, groundHeight;
    @Getter
    private Integer[] heroX, heroY, heroSpace;
    private int nextX, nextY, nextWidth, nextHeight;
    private int exitX, exitY, exitWidth, exitHeight;
    private int eventLogX, eventLogY, eventLogWidth, eventLogHeight;

    public PlayPanel(PlayAction playAction) {
        setLayout(null);
        this.playAction = playAction;
        this.background = ImageLoader.getInstance().getBackground("play");
        config();
        initialize();
        this.setBounds(x, y, width, height);
        addComponents();
        animationManger = new AnimationManger();
        manaImage = ImageLoader.getInstance().getEffect("mana");
        executor = new PlayEventExecutor(this);
    }

    private void addComponents() {
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
        this.add(progressBar);
        this.add(scrollPane);
        this.add(exit);
        this.add(next);
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
        initializeProgressBar();
    }

    private void initializeProgressBar() {
        progressBar = new JProgressBar(0, 60000);
        progressBar.setBounds(groundX[1], groundY[1] + ground[1].getHeight()
                , ground[1].getWidth(), groundY[0] - groundY[1] - ground[1].getHeight());
        progressBar.setOpaque(false);

    }

    private void initializeBox(Box<?, ?> box, String title, int x, int y) {
        box.setTitle(title);
        box.setLocation(x, y);
    }

    private void initializeHands() {
        hand = new CardBox[2];
        hand[0] = new IndexedCardBox(handWidth[0], handHeight[0], this
                , cardName -> playAction.selectCardInHand(0, cardName));
        hand[1] = new IndexedCardBox(handWidth[1], handHeight[1], this
                , cardName -> playAction.selectCardInHand(1, cardName));
        initializeBox(hand[0], "Your hand card", handX[0], handY[0]);
        initializeBox(hand[1], "Opponent hand card", handX[1], handY[1]);
    }

    private void initializeGrounds() {
        ground = new MinionBox[2];
        ground[0] = new MinionBox(groundWidth[0], groundHeight[0], this
                , cardName -> playAction.selectMinion(0, cardName));
        ground[1] = new MinionBox(groundWidth[1], groundHeight[1], this
                , cardName -> playAction.selectMinion(1, cardName));
        initializeBox(ground[0], "", groundX[0], groundY[0]);
        initializeBox(ground[1], "", groundX[1], groundY[1]);
    }

    private void initializeViewer(UnitViewer unitViewer, int x, int y, int width, int height) {
        unitViewer.setBounds(x, y, width, height);
    }

    private void initializeHero() {
        hero = new UnitViewer[2];
        hero[0] = new UnitViewer(this);
        hero[0].setActionListener(cardName -> playAction.selectHero(0));
        initializeViewer(hero[0], heroX[0], heroY[0], HERO_WIDTH, HERO_HEIGHT);
        hero[1] = new UnitViewer(this);
        hero[1].setActionListener(cardName -> playAction.selectHero(1));
        initializeViewer(hero[1], heroX[1], heroY[1], HERO_WIDTH, HERO_HEIGHT);
    }

    private void initializeWeapon() {
        weapon = new UnitViewer[2];
        weapon[0] = new UnitViewer(this);
        int x = heroX[0] - heroSpace[0] - WEAPON_WIDTH;
        initializeViewer(weapon[0], x, heroY[0], WEAPON_WIDTH, WEAPON_HEIGHT);
        weapon[1] = new UnitViewer(this);
        x = heroX[1] - heroSpace[1] - WEAPON_WIDTH;
        initializeViewer(weapon[1], x, heroY[1], WEAPON_WIDTH, WEAPON_HEIGHT);
    }

    private void initializeHeroPower() {
        heroPower = new UnitViewer[2];
        heroPower[0] = new UnitViewer(this);
        heroPower[0].setActionListener(cardName -> playAction.selectHeroPower(0));
        int x = heroX[0] + heroSpace[0] + HERO_WIDTH;
        initializeViewer(heroPower[0], x, heroY[0], HERO_POWER_WIDTH, HERO_POWER_HEIGHT);
        heroPower[1] = new UnitViewer(this);
        heroPower[1].setActionListener(cardName -> playAction.selectHeroPower(1));
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
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        eventLog.setBorder(null);
        eventLog.setOpaque(false);
        eventLog.setForeground(Color.WHITE);
        eventLog.setEditable(false);
        scrollPane.getViewport().setOpaque(false);
    }

    public void setDetails(List<PlayDetails.Event> events, String eventLog, int[] mana, long time) {
        events.forEach(executor::execute);
        animationManger.start();
        this.eventLog.setText(eventLog);
        this.mana[0] = mana[0];
        this.mana[1] = mana[1];
        this.time = time;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.background, 0, 0, null);
        animationManger.paint((Graphics2D) g);
        g.setColor(Color.WHITE);
        for (int i = 0; i < this.mana[0]; i++) {
            g.drawImage(manaImage, manaX[0], manaY[0] - i * (manaImage.getHeight() + manaSpace), null);
        }
        for (int i = 0; i < mana[1]; i++) {
            g.drawImage(manaImage, manaX[1], manaY[1] + i * (manaImage.getHeight() + manaSpace), null);
        }

        int t = (int) (System.currentTimeMillis() - time);
        if (t < 1000) progressBar.setForeground(Color.GREEN);
        else if (40000 < t && t < 41000) progressBar.setForeground(Color.RED);
        progressBar.setValue(t);
    }

    public void exit() {
        playAction.exit();
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
