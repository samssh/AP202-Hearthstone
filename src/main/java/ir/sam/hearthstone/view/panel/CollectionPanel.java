package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.Actions.CollectionAction;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.util.Updatable;
import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.effects.LinearMotion;
import ir.sam.hearthstone.view.graphics_engine.effects.OverviewPainter;
import ir.sam.hearthstone.view.graphics_engine.effects.Rotary;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.SmallDeckOverview;
import ir.sam.hearthstone.view.util.CardBox;
import ir.sam.hearthstone.view.util.Constant;
import ir.sam.hearthstone.view.util.MyChangeListener;
import ir.sam.hearthstone.view.util.SmallDeckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class CollectionPanel extends JPanel implements Updatable {
    private JLabel label;
    private JTextField search;
    private JComboBox<String> mana, classOfCard, lockMode;
    private CardBox cards, deckCards;
    private SmallDeckBox decks;
    private JButton newDeck, deleteDeck, changeDeckName, changeHeroDeck;
    private JButton back, backMainMenu, exit;
    private List<String> heroNames;
    private String deckName;
    private final AnimationManger animationManger;
    private final BufferedImage image;
    private final CollectionAction collectionAction;
    private int x, y, width, height;
    private int exitX, exitY, exitWidth, exitHeight, exitSpace;
    private int cardsX, cardsY, cardsWidth, cardsHeight;
    private int deckCardsX, deckCardsY, deckCardsWidth, deckCardsHeight;
    private int decksX, decksY, decksWidth, decksHeight;
    private int deckButtonX, deckButtonY, deckButtonWidth, deckButtonHeight, deckButtonSpace;
    private int filterX, filterY, filterWidth, filterHeight, filterSpace;

    public CollectionPanel(CollectionAction collectionAction) {
        this.collectionAction = collectionAction;
        this.setLayout(null);
        this.config();
        this.setBounds(x, y, width, height);
        this.image = ImageLoader.getInstance().getBackground("collection");
        initialize();
        this.add(label);
        this.add(search);
        this.add(mana);
        this.add(lockMode);
        this.add(classOfCard);
        this.add(cards);
        this.add(decks);
        this.add(exit);
        this.add(back);
        this.add(backMainMenu);
        animationManger = new AnimationManger();
    }

    private void initialize() {
        initializeLabel();
        initializeSearch();
        initializeMana();
        initializeClassOfCard();
        initializeLockMode();
        initializeCards();
        initializeDeckCard();
        initializeDecks();
        initializeNewDeck();
        initializeDeleteDeck();
        initializeChangeDeckName();
        initializeChangeHeroDeck();
        initializeBack();
        initializeBackMainMenu();
        initializeExit();
    }

    private void initializeLabel() {
        label = new JLabel("search:", JLabel.RIGHT);
        label.setBounds(filterX, filterY, filterWidth, filterHeight);
    }

    private void initializeSearch() {
        search = new JTextField();
        int x = filterX + filterWidth + filterSpace;
        search.setBounds(x, filterY, filterWidth, filterHeight);
        search.getDocument().addDocumentListener((MyChangeListener) collectionAction::search);

    }

    private void initializeClassOfCard() {
        classOfCard = new JComboBox<>();
        int x = filterX + 2 * (filterWidth + filterSpace);
        classOfCard.setBounds(x, filterY, filterWidth, filterHeight);
        classOfCard.addItemListener(collectionAction::classOfCard);
    }

    private void initializeMana() {
        mana = new JComboBox<>(new String[]{"all", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        mana.setMaximumRowCount(4);
        int x = filterX + 3 * (filterWidth + filterSpace);
        mana.setBounds(x, filterY, filterWidth, filterHeight);
        mana.addItemListener(collectionAction::mana);
    }

    private void initializeLockMode() {
        lockMode = new JComboBox<>(new String[]{"all cards", "locked cards", "unlocked cards"});
        int x = filterX + 4 * (filterWidth + filterSpace);
        lockMode.setBounds(x, filterY, filterWidth, filterHeight);
        lockMode.addItemListener(collectionAction::lockMode);
    }

    private void initializeCards() {
        cards = new CardBox(cardsWidth, cardsHeight, this, collectionAction::addCardToDeck, true);
        cards.setLocation(cardsX, cardsY);
    }

    private void initializeDeckCard() {
        deckCards = new CardBox(deckCardsWidth, deckCardsHeight, this, collectionAction::removeCardFromDeck, true);
        deckCards.setLocation(deckCardsX, deckCardsY);
    }

    private void initializeDecks() {
        decks = new SmallDeckBox(decksWidth, decksHeight, this, collectionAction::selectDeck);
        decks.setLocation(decksX, decksY);

    }

    private void initializeNewDeck() {
        newDeck = new JButton("new deck");
        newDeck.setBounds(deckButtonX, deckButtonY, deckButtonWidth, deckButtonHeight);
        newDeck.addActionListener(this::newDeck);
        Constant.makeTransparent(newDeck);
    }

    private void newDeck(ActionEvent event) {
        String deckName = JOptionPane.showInputDialog(this, "enter deck name",
                "new deck", JOptionPane.INFORMATION_MESSAGE);
        if (deckName != null) {
            String heroName = (String) JOptionPane.showInputDialog(this, "select hero",
                    "new deck", JOptionPane.INFORMATION_MESSAGE, null, heroNames.toArray(), heroNames.get(0));
            collectionAction.newDeck(deckName, heroName);
        }
    }

    private void initializeDeleteDeck() {
        deleteDeck = new JButton("delete deck");
        int x = deckButtonX + (deckButtonWidth + deckButtonSpace);
        deleteDeck.setBounds(x, deckButtonY, deckButtonWidth, deckButtonHeight);
        deleteDeck.addActionListener(e -> collectionAction.deleteDeck(deckName));
        Constant.makeTransparent(deleteDeck);
    }

    private void initializeChangeDeckName() {
        changeDeckName = new JButton("change name");
        int x = deckButtonX + 2 * (deckButtonWidth + deckButtonSpace);
        changeDeckName.setBounds(x, deckButtonY, deckButtonWidth, deckButtonHeight);
        changeDeckName.addActionListener(this::changeDeckName);
        Constant.makeTransparent(changeDeckName);
    }

    private void changeDeckName(ActionEvent event) {
        String newName = JOptionPane.showInputDialog(this, "enter new deck name",
                "change deck name", JOptionPane.INFORMATION_MESSAGE);
        if (newName != null) {
            collectionAction.changeDeckName(deckName, newName);
        }
    }

    private void initializeChangeHeroDeck() {
        changeHeroDeck = new JButton("change hero");
        int x = deckButtonX + 3 * (deckButtonWidth + deckButtonSpace);
        changeHeroDeck.setBounds(x, deckButtonY, deckButtonWidth, deckButtonHeight);
        changeHeroDeck.addActionListener(this::changeHeroDeck);
        Constant.makeTransparent(changeHeroDeck);
    }

    private void changeHeroDeck(ActionEvent event) {
        String newHeroName = (String) JOptionPane.showInputDialog(this, "select hero",
                "new deck", JOptionPane.INFORMATION_MESSAGE, null, heroNames.toArray(), heroNames.get(0));
        if (newHeroName != null) {
            collectionAction.changeHeroDeck(deckName, newHeroName);
        }
    }

    private void initializeExit() {
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> collectionAction.exit());
        Constant.makeTransparent(exit);
    }

    private void initializeBack() {
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(e -> collectionAction.back());
        Constant.makeTransparent(back);
    }

    private void initializeBackMainMenu() {
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(e -> collectionAction.backMainMenu());
        Constant.makeTransparent(backMainMenu);
    }

    public void putDeckEvent(String type,String deckName,SmallDeckOverview newDeck){
        switch (type) {
            case "change":
                decks.changeModel(deckName, newDeck);
                break;
            case "delete":
                decks.removeModel(deckName, true);
                break;
            case "new":
                decks.addModel(newDeck);
                break;
            default:
                System.err.println("shit");
                break;
        }
    }

    public void putCardEvent(String type,String cardName,boolean canAddDeck, boolean canChangeHero){
        switch (type) {
            case "add":
                moveCard(cardName, cards, deckCards);
                break;
            case "move":
                moveCard(cardName, deckCards, cards);
                break;
            case "remove":
                deckCards.removeModel(cardName, true);
                break;
            default:
                System.err.println("shit");
                break;
        }
        setButtons(canAddDeck,canChangeHero);
    }

    private void moveCard(String cardName,CardBox origin,CardBox dest){
        Point org = origin.getPosition(cardName);
        org.translate(origin.getX(),origin.getY());
        CardOverview cardOverview = origin.removeModel(cardName,false);
        dest.addModel(cardOverview,false);
        Point des = dest.getPosition(cardName);
        des.translate(dest.getX(),dest.getY());
        animationManger.clear();
        animationManger.addPainter(new LinearMotion(org.x,org.y,des.x,des.y,
                new Rotary(new OverviewPainter(cardOverview)), x->Math.pow(x,1/2.5)));
        animationManger.start();
    }

    public void setDetails(List<CardOverview> cards, List<SmallDeckOverview> decks,
                           List<CardOverview> deckCards, boolean canAddDeck, boolean canChangeHero,
                           String deckName, List<String> heroNames, List<String> classOfCardNames) {
        setCards(cards);
        setDecks(decks);
        setDeckCards(deckName, deckCards);
        setButtons(canAddDeck, canChangeHero);
        setHeroNames(heroNames);
        setClassOfCard(classOfCardNames);
        collectionAction.setDeckName(deckName);
    }

    private void setClassOfCard(List<String> classOfCardNames) {
        if (classOfCardNames != null) {
            classOfCard.removeAllItems();
            ItemListener itemListener = classOfCard.getItemListeners()[0];
            classOfCard.removeItemListener(itemListener);
            classOfCard.addItem("All classes");
            classOfCard.addItemListener(itemListener);
            classOfCardNames.forEach(classOfCard::addItem);
        }
    }

    private void setHeroNames(List<String> heroNames) {
        if (heroNames != null)
            this.heroNames = heroNames;
    }

    private void setButtons(boolean canAddDeck, boolean canChangeHero) {
        if (canAddDeck) this.add(newDeck);
        else this.remove(newDeck);
        if (canChangeHero) this.add(changeHeroDeck);
        else this.remove(changeHeroDeck);
    }

    private void setCards(List<CardOverview> cards) {
        if (cards != null)
            this.cards.setModels(cards);
    }

    private void setDecks(List<SmallDeckOverview> decks) {
        if (decks != null)
            this.decks.setModels(decks);
    }

    private void setDeckCards(String deckName, List<CardOverview> deckCards) {
        this.deckName = deckName;
        if (deckCards != null) {
            this.add(this.deckCards);
            this.deckCards.setModels(deckCards);
            this.deckCards.setTitle(deckName);
            this.add(changeDeckName);
            this.add(deleteDeck);
        } else {
            this.remove(this.deckCards);
            this.remove(changeDeckName);
            this.remove(deleteDeck);
        }
    }

    private void config() {
        Config config = ConfigFactory.getInstance().getConfig("COLLECTION_CONFIG");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");
        exitX = config.getProperty(Integer.class, "exitX");
        exitY = config.getProperty(Integer.class, "exitY");
        cardsX = config.getProperty(Integer.class, "cardsX");
        cardsY = config.getProperty(Integer.class, "cardsY");
        exitWidth = config.getProperty(Integer.class, "exitWidth");
        exitHeight = config.getProperty(Integer.class, "exitHeight");
        exitSpace = config.getProperty(Integer.class, "exitSpace");
        cardsWidth = config.getProperty(Integer.class, "cardsWidth");
        cardsHeight = config.getProperty(Integer.class, "cardsHeight");
        deckCardsX = config.getProperty(Integer.class, "deckCardsX");
        deckCardsY = config.getProperty(Integer.class, "deckCardsY");
        deckCardsWidth = config.getProperty(Integer.class, "deckCardsWidth");
        deckCardsHeight = config.getProperty(Integer.class, "deckCardsHeight");
        decksX = config.getProperty(Integer.class, "decksX");
        decksY = config.getProperty(Integer.class, "decksY");
        decksWidth = config.getProperty(Integer.class, "decksWidth");
        decksHeight = config.getProperty(Integer.class, "decksHeight");
        deckButtonX = config.getProperty(Integer.class, "deckButtonX");
        deckButtonY = config.getProperty(Integer.class, "deckButtonY");
        deckButtonWidth = config.getProperty(Integer.class, "deckButtonWidth");
        deckButtonHeight = config.getProperty(Integer.class, "deckButtonHeight");
        deckButtonSpace = config.getProperty(Integer.class, "deckButtonSpace");
        filterX = config.getProperty(Integer.class, "filterX");
        filterY = config.getProperty(Integer.class, "filterY");
        filterWidth = config.getProperty(Integer.class, "filterWidth");
        filterHeight = config.getProperty(Integer.class, "filterHeight");
        filterSpace = config.getProperty(Integer.class, "filterSpace");
    }

    public void reset() {
        collectionAction.reset();
    }

    @Override
    public void update() {
        collectionAction.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        animationManger.paint((Graphics2D) g);
    }
}