package view.panel;

import client.Client;
import configs.Config;
import configs.ConfigFactory;
import view.model.CardOverview;
import view.model.HeroOverview;
import view.model.HeroPowerOverview;
import view.util.CardBox;
import view.util.UnitViewer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static view.util.Constant.*;

public class PlayPanel extends JPanel {
    private CardBox hand, ground;
    private UnitViewer hero, weapon, heroPower;
    private JButton exit, next;
    private JTextArea eventLog;
    private JScrollPane scrollPane;
    private int mana, deckCards;
    private final Client.PlayAction playAction;
    private int x, y, width, height;
    private int manaX, manaY, manaSpace;
    private int handX, handY, handWidth, handHeight;
    private int groundX, groundY, groundWidth, groundHeight;
    private int heroX, heroY, heroSpace;
    private int nextX, nextY, nextWidth, nextHeight;
    private int exitX, exitY, exitWidth, exitHeight;
    private int eventLogX, eventLogY, eventLogWidth, eventLogHeight;

    public PlayPanel(Client.PlayAction playAction) {
        setLayout(null);
        this.playAction = playAction;
        config();
        initialize();
        this.setBounds(x,y,width,height);
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
        hand = new CardBox(handWidth, handHeight, this, playAction::playCard);
        hand.setTitle("Hand cards");
        hand.setLocation(handX, handY);
    }

    private void initializeGround() {
        ground = new CardBox(groundWidth, groundHeight, this, null);
        ground.setLocation(groundX, groundY);
        ground.setTitle("Ground cards");
    }

    private void initializeHero() {
        hero = new UnitViewer(this);
        hero.setLocation(heroX, heroY);
        hero.setSize(HERO_WIDTH, HERO_HEIGHT);
    }

    private void initializeWeapon() {
        weapon = new UnitViewer(this);
        int x = heroX - heroSpace - CARD_WIDTH;
        weapon.setLocation(x, heroY);
        weapon.setSize(CARD_WIDTH, CARD_HEIGHT);
    }

    private void initializeHeroPower() {
        heroPower = new UnitViewer(this);
        int x = heroX + heroSpace + HERO_WIDTH;
        heroPower.setLocation(x, heroY);
        heroPower.setSize(HERO_POWER_WIDTH, HERO_POWER_HEIGHT);
    }

    private void initializeExit() {
        exit = new JButton("End Game");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> playAction.exit());
    }

    private void initializeNext() {
        next = new JButton("Next Turn");
        next.setBounds(nextX, nextY, nextWidth, nextHeight);
        next.addActionListener(e -> playAction.endTurn());
    }

    private void initializeEventLog() {
        eventLog = new JTextArea();
        scrollPane = new JScrollPane(eventLog);
        scrollPane.setBounds(eventLogX, eventLogY, eventLogWidth, eventLogHeight);

    }

    public void setDetails(List<CardOverview> hand, List<CardOverview> ground, CardOverview weapon,
                           HeroOverview hero, HeroPowerOverview heroPower, String eventLog, int mana, int deckCards) {
        this.hand.setModels(hand);
        this.ground.setModels(ground);
        this.weapon.setUnitOverview(weapon);
        this.hero.setUnitOverview(hero);
        this.heroPower.setUnitOverview(heroPower);
        this.eventLog.setText(eventLog);
        this.mana = mana;
        this.deckCards = deckCards;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
