package view.panel;

import client.Client;
import util.Config;
import util.ConfigFactory;
import util.ImageLoader;
import util.Updatable;
import view.model.CardOverview;
import view.model.SmallDeckOverview;
import view.util.CardBox;
import view.util.Constant;
import view.util.MyChangeListener;
import view.util.SmallDeckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private final BufferedImage image;
    private final Client.CollectionAction collectionAction;
    private int x, y, width, height;
    private int exitX, exitY, exitWidth, exitHeight, exitSpace;
    private int cardsX, cardsY, cardsWidth, cardsHeight;
    private int deckCardsX, deckCardsY, deckCardsWidth, deckCardsHeight;
    private int decksX, decksY, decksWidth, decksHeight;
    private int deckButtonX, deckButtonY, deckButtonWidth, deckButtonHeight, deckButtonSpace;
    private int filterX, filterY, filterWidth, filterHeight, filterSpace;

    public CollectionPanel(Client.CollectionAction collectionAction) {
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
        search.getDocument().addDocumentListener((MyChangeListener)collectionAction::search);

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
        cards = new CardBox(cardsWidth, cardsHeight, this, collectionAction::addCardToDeck);
        cards.setLocation(cardsX, cardsY);
    }

    private void initializeDeckCard() {
        deckCards = new CardBox(deckCardsWidth, deckCardsHeight, this, collectionAction::removeCardFromDeck);
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
        if (newHeroName!=null){
            collectionAction.changeHeroDeck(deckName,newHeroName);
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

    public void setFirstDetails(List<String> heroNames, List<String> classOfCardNames) {
        collectionAction.sendRequest();
        this.heroNames = heroNames;
        classOfCard.removeAllItems();
        classOfCard.addItem("All classes");
        classOfCardNames.forEach(classOfCard::addItem);
    }

    public void setDetails(List<CardOverview> cards, List<SmallDeckOverview> decks,
                           List<CardOverview> deckCards, boolean canAddDeck, boolean canChangeHero, String deckName) {
        this.cards.setModels(cards);
        this.decks.setModels(decks);
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
        if (canAddDeck) this.add(newDeck);
        else this.remove(newDeck);
        if (canChangeHero) this.add(changeHeroDeck);
        else this.remove(changeHeroDeck);
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

    public boolean hasFirst(){
        return classOfCard.getItemCount()>0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
    }
}