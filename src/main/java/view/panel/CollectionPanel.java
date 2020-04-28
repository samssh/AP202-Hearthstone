package view.panel;

import configs.Config;
import configs.ConfigFactory;
import view.util.CardBox;
import view.util.DeckBox;

import javax.swing.*;

public class CollectionPanel extends JPanel {
    private JTextField search;
    private JComboBox<String> mana, classOfCard, lockMode;
    private CardBox collection, deckCard;
    private DeckBox deckBox;
    private JButton newDeck, deleteDeck, changeDeckName, changeHeroDeck;
    private JButton back, backMainMenu, exit;
    private int x, y, width, height;
    private int exitX, exitY, exitWidth, exitHeight, exitSpace;
    private int collectionX, collectionY, collectionWidth, collectionHeight;
    private int deckCardX, deckCardY, deckCardWidth, deckCardHeight;
    private int deckBoxX, deckBoxY, deckBoxWidth, deckBoxHeight;
    private int deckButtonX, deckButtonY, deckButtonWidth, deckButtonHeight, deckButtonSpace;
    private int filterX, filterY, filterWidth, filterHeight, filterSpace;

    public CollectionPanel() {
        this.setLayout(null);
        this.config();
        this.setBounds(x, y, width, height);
        initialize();
        this.add(search);
        this.add(mana);
        this.add(lockMode);
        this.add(classOfCard);
        this.add(collection);
        this.add(deckCard);
        this.add(deckBox);
        this.add(newDeck);
        this.add(deleteDeck);
        this.add(changeDeckName);
        this.add(changeHeroDeck);
        this.add(exit);
        this.add(back);
        this.add(backMainMenu);
    }

    private void initialize() {
        initializeSearch();
        initializeMana();
        initializeClassOfCard();
        initializeLockMode();
        initializeCollection();
        initializeDeckCard();
        initializeDeckBox();
        initializeNewDeck();
        initializeDeleteDeck();
        initializeChangeDeckName();
        initializeChangeHeroDeck();
        initializeBack();
        initializeBackMainMenu();
        initializeExit();
    }

    private void initializeSearch() {
        search = new JTextField();
        search.setBounds(filterX, filterY, filterWidth, filterHeight);
    }

    private void initializeClassOfCard() {
        classOfCard = new JComboBox<>();
        int x = filterX + filterWidth + filterSpace;
        classOfCard.setBounds(x, filterY, filterWidth, filterHeight);
    }

    private void initializeMana() {
        mana = new JComboBox<>();
        int x = filterX + 2 * (filterWidth + filterSpace);
        mana.setBounds(x, filterY, filterWidth, filterHeight);
    }

    private void initializeLockMode() {
        lockMode = new JComboBox<>();
        int x = filterX + 3 * (filterWidth + filterSpace);
        lockMode.setBounds(x, filterY, filterWidth, filterHeight);
    }

    private void initializeCollection() {
        collection = new CardBox(collectionWidth, collectionHeight, this, null);
        collection.setLocation(collectionX, collectionY);
    }

    private void initializeDeckCard() {
        deckCard = new CardBox(deckCardWidth, deckCardHeight, this, null);
        deckCard.setLocation(deckCardX, deckCardY);
    }

    private void initializeDeckBox() {
        deckBox = new DeckBox(deckBoxWidth, deckBoxHeight, this, null);
        deckBox.setLocation(deckBoxX, deckBoxY);
    }

    private void initializeNewDeck() {
        newDeck = new JButton();
        newDeck.setBounds(deckButtonX, deckButtonY, deckBoxWidth, deckButtonHeight);
    }

    private void initializeDeleteDeck() {
    }

    private void initializeChangeDeckName() {
    }

    private void initializeChangeHeroDeck() {
    }


    private void initializeExit() {
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(null);
    }

    private void initializeBack() {
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(null);
    }

    private void initializeBackMainMenu() {
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(null);
    }

    private void config() {
        Config config = ConfigFactory.getInstance("").getConfig("COLLECTION_CONFIG");
        x = config.getProperty(Integer.class, "x");
        y = config.getProperty(Integer.class, "y");
        width = config.getProperty(Integer.class, "width");
        height = config.getProperty(Integer.class, "height");

    }
}