package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.Actions.PlayAction;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.HeroOverview;
import ir.sam.hearthstone.view.model.HeroPowerOverview;
import ir.sam.hearthstone.view.util.CardBox;
import ir.sam.hearthstone.view.util.Constant;
import ir.sam.hearthstone.view.util.UnitViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayPanel extends JPanel {
    private CardBox hand, ground;
    private UnitViewer hero, weapon, heroPower;
    private JButton exit, next;
    private JTextArea eventLog;
    private JScrollPane scrollPane;
    private int mana, deckCards;
    private final PlayAction playAction;
    private final BufferedImage image;
    private int x, y, width, height;
    private int manaX, manaY, manaSpace;
    private int handX, handY, handWidth, handHeight;
    private int groundX, groundY, groundWidth, groundHeight;
    private int heroX, heroY, heroSpace;
    private int nextX, nextY, nextWidth, nextHeight;
    private int exitX, exitY, exitWidth, exitHeight;
    private int eventLogX, eventLogY, eventLogWidth, eventLogHeight;

    public PlayPanel(PlayAction playAction) {
        setLayout(null);
        this.playAction = playAction;
        this.image = ImageLoader.getInstance().getBackground("play");
        config();
        initialize();
        this.setBounds(x, y, width, height);
        this.add(hand);
        this.add(ground);
        this.add(hero);
        this.add(weapon);
        this.add(heroPower);
        this.add(scrollPane);
        this.add(exit);
        this.add(next);
    }

    private void initialize() {
        initializeHand();
        initializeGround();
        initializeHero();
        initializeWeapon();
        initializeHeroPower();
        initializeExit();
        initializeNext();
        initializeEventLog();
    }

    private void initializeHand() {
        hand = new CardBox(handWidth, handHeight, this, playAction::playCard, false);
        hand.setTitle("Hand cards");
        hand.setLocation(handX, handY);
    }

    private void initializeGround() {
        ground = new CardBox(groundWidth, groundHeight, this, null, false);
        ground.setLocation(groundX, groundY);
        ground.setTitle("Ground cards");
    }

    private void initializeHero() {
        hero = new UnitViewer(this);
        hero.setLocation(heroX, heroY);
        hero.setSize(Constant.HERO_WIDTH, Constant.HERO_HEIGHT);
    }

    private void initializeWeapon() {
        weapon = new UnitViewer(this);
        int x = heroX - heroSpace - Constant.CARD_WIDTH;
        weapon.setLocation(x, heroY);
        weapon.setSize(Constant.CARD_WIDTH, Constant.CARD_HEIGHT);
    }

    private void initializeHeroPower() {
        heroPower = new UnitViewer(this);
        int x = heroX + heroSpace + Constant.HERO_WIDTH;
        heroPower.setLocation(x, heroY);
        heroPower.setSize(Constant.HERO_POWER_WIDTH, Constant.HERO_POWER_HEIGHT);
    }

    private void initializeExit() {
        exit = new JButton("End Game");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> playAction.exit());
        Constant.makeTransparent(exit);
        exit.setBackground(new Color(240, 240, 240, 100));
    }

    private void initializeNext() {
        next = new JButton("Next Turn");
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

    public void setDetails(List<CardOverview> hand, List<CardOverview> ground, CardOverview weapon,
                           HeroOverview hero, HeroPowerOverview heroPower, String eventLog, int mana, int deckCards) {
        this.hand.setModels(hand);
        this.weapon.setUnitOverview(weapon);
        /*if (weapon!=null) this.ground.addModel(1,weapon);
        else*/ this.ground.setModels(ground);
        this.hero.setUnitOverview(hero);
        this.heroPower.setUnitOverview(heroPower);
        this.eventLog.setText(eventLog);
        this.mana = mana;
        this.deckCards = deckCards;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawString("Mana:" + mana, manaX, manaY);
        g.drawString("Deck Cards:" + deckCards, manaX, manaY + manaSpace);
    }

    private void config() {
        Config config = ConfigFactory.getInstance().getConfig("PLAY_PANEL");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");
        handX = config.getProperty(Integer.class, "handX");
        handY = config.getProperty(Integer.class, "handY");
        handWidth = config.getProperty(Integer.class, "handWidth");
        handHeight = config.getProperty(Integer.class, "handHeight");
        groundX = config.getProperty(Integer.class, "groundX");
        groundY = config.getProperty(Integer.class, "groundY");
        groundWidth = config.getProperty(Integer.class, "groundWidth");
        groundHeight = config.getProperty(Integer.class, "groundHeight");
        heroX = config.getProperty(Integer.class, "heroX");
        heroY = config.getProperty(Integer.class, "heroY");
        heroSpace = config.getProperty(Integer.class, "heroSpace");
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
        manaX = config.getProperty(Integer.class, "manaX");
        manaY = config.getProperty(Integer.class, "manaY");
        manaSpace = config.getProperty(Integer.class, "manaSpace");
    }
}
