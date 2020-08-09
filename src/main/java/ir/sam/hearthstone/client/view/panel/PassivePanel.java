package ir.sam.hearthstone.client.view.panel;

import ir.sam.hearthstone.client.controller.action_listener.PassiveAction;
import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.Overview;
import ir.sam.hearthstone.client.model.main.PassiveOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import ir.sam.hearthstone.client.resource_manager.Config;
import ir.sam.hearthstone.client.resource_manager.ConfigFactory;
import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import ir.sam.hearthstone.client.view.util.Box;
import ir.sam.hearthstone.client.view.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PassivePanel extends JPanel {
    private PassiveBox passiveBox;
    private SmallDeckBox deckBox;
    private IndexedCardBox cardBox;
    private JButton button;
    private final PassiveAction passiveAction;
    private JButton exit, back, backMainMenu;
    private final BufferedImage image;
    private int exitX, exitY, exitWidth, exitHeight, exitSpace;
    private int x, y, width, height;
    private int passiveBoxX, passiveBoxY, passiveBoxWidth, passiveBoxHeight;
    private int deckBoxX, deckBoxY, deckBoxWidth, deckBoxHeight;
    private int cardBoxX, cardBoxY, cardBoxWidth, cardBoxHeight;
    private int buttonX, buttonY, buttonWidth, buttonHeight;

    public PassivePanel(PassiveAction passiveAction) {
        setLayout(null);
        this.passiveAction = passiveAction;
        config();
        this.setBounds(x, y, width, height);
        this.image = ImageLoader.getInstance().getBackground("passive");
        initialize();
        this.add(exit);
        this.add(back);
        this.add(backMainMenu);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    private void initialize() {
        initializePassiveBox();
        initializeDeckBox();
        initializeCardBox();
        initializeButton();
        initializeBack();
        initializeExit();
        initializeBackMainMenu();
    }

    private void initializeButton() {
        button = new JButton("Confirm");
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        button.addActionListener(e -> passiveAction.confirm());
        Constant.makeTransparent(button);
    }

    private void initializeCardBox() {
        cardBox = new IndexedCardBox(cardBoxWidth, cardBoxHeight, this, passiveAction::selectCard);
        cardBox.setLocation(cardBoxX, cardBoxY);
    }

    private void initializePassiveBox() {
        passiveBox = new PassiveBox(passiveBoxWidth, passiveBoxHeight, this, passiveAction::selectPassive);
        passiveBox.setLocation(passiveBoxX, passiveBoxY);
    }

    private void initializeDeckBox() {
        deckBox = new SmallDeckBox(deckBoxWidth, deckBoxHeight, this, passiveAction::selectDeck);
        deckBox.setLocation(deckBoxX, deckBoxY);
    }

    private void initializeExit() {
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> passiveAction.exit());
        Constant.makeTransparent(exit);
    }

    private void initializeBack() {
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(e -> passiveAction.back());
        Constant.makeTransparent(back);
    }

    private void initializeBackMainMenu() {
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(e -> passiveAction.backMainMenu());
        Constant.makeTransparent(backMainMenu);
    }

    public void setDetails(List<PassiveOverview> passives, List<SmallDeckOverview> decks
            , List<CardOverview> cards, String message, boolean showButton) {
        setModels(passiveBox, passives, message);
        setModels(deckBox, decks, message);
        setModels(cardBox, cards, message);
        if (showButton) this.add(button);
        else this.remove(button);
    }

    public void changeCard(CardOverview cardOverview, int index) {
        cardBox.changeModel(index, cardOverview);
    }

    private <Model extends Overview> void setModels(Box<Model, ?> box, List<Model> models, String message) {
        if (models != null) {
            box.setModels(models);
            box.setTitle(message);
            this.add(box);
        } else {
            this.remove(box);
        }
    }

    void config() {
        Config config = ConfigFactory.getInstance().getConfig("PASSIVE_CONFIG");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");
        exitX = config.getProperty(Integer.class, "exitX");
        exitY = config.getProperty(Integer.class, "exitY");
        exitWidth = config.getProperty(Integer.class, "exitWidth");
        exitHeight = config.getProperty(Integer.class, "exitHeight");
        exitSpace = config.getProperty(Integer.class, "exitSpace");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        passiveBoxX = config.getProperty(Integer.class, "passiveBoxX");
        passiveBoxY = config.getProperty(Integer.class, "passiveBoxY");
        passiveBoxWidth = config.getProperty(Integer.class, "passiveBoxWidth");
        passiveBoxHeight = config.getProperty(Integer.class, "passiveBoxHeight");
        deckBoxX = config.getProperty(Integer.class, "deckBoxX");
        deckBoxY = config.getProperty(Integer.class, "deckBoxY");
        deckBoxWidth = config.getProperty(Integer.class, "deckBoxWidth");
        deckBoxHeight = config.getProperty(Integer.class, "deckBoxHeight");
        cardBoxX = config.getProperty(Integer.class, "cardBoxX");
        cardBoxY = config.getProperty(Integer.class, "cardBoxY");
        cardBoxWidth = config.getProperty(Integer.class, "cardBoxWidth");
        cardBoxHeight = config.getProperty(Integer.class, "cardBoxHeight");
        buttonX = config.getProperty(Integer.class, "buttonX");
        buttonY = config.getProperty(Integer.class, "buttonY");
        buttonWidth = config.getProperty(Integer.class, "buttonWidth");
        buttonHeight = config.getProperty(Integer.class, "buttonHeight");
    }
}
