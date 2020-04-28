package view.panel;

import client.Client;
import configs.Config;
import configs.ConfigFactory;
import view.model.DeckOverview;
import view.util.DeckBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusPanel extends JPanel {
    private DeckBox deckBox;
    private JButton exit, back, backMainMenu;
    private int deckBoxX,deckBoxY,deckBoxWidth,deckBoxHeight;
    private int exitX,exitY,exitWidth,exitHeight,exitSpace;
    private final Client.StatusAction statusAction;

    public StatusPanel(Client.StatusAction statusAction) {
        setLayout(null);
        this.statusAction = statusAction;
        config();
        initialize();
        this.add(deckBox);
        this.add(exit);
        this.add(back);
        this.add(backMainMenu);
    }

    private void initialize() {
        initializeBack();
        initializeBackMainMenu();
        initializeExit();
        initializeDeckBox();
    }

    private void initializeDeckBox() {
        deckBox = new DeckBox(deckBoxWidth,deckBoxHeight,this,null);
        deckBox.setLocation(deckBoxX,deckBoxY);
        deckBox.setTitle("your best deck");
    }


    private void initializeExit(){
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(statusAction::exit);
    }

    private void initializeBack(){
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(statusAction::back);
    }

    private void initializeBackMainMenu(){
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(statusAction::backMainMenu);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void config() {
        Config shopConfig = ConfigFactory.getInstance("").getConfig("STATUS_CONFIG");
        setBounds(shopConfig.getProperty(Integer.class, "x"),
                shopConfig.getProperty(Integer.class, "y"),
                shopConfig.getProperty(Integer.class, "width"),
                shopConfig.getProperty(Integer.class, "height"));
        deckBoxX = shopConfig.getProperty(Integer.class, "deckBoxX");
        deckBoxY = shopConfig.getProperty(Integer.class, "deckBoxY");
        exitX = shopConfig.getProperty(Integer.class, "exitX");
        exitY = shopConfig.getProperty(Integer.class, "exitY");
        deckBoxWidth = shopConfig.getProperty(Integer.class, "deckBoxWidth");
        deckBoxHeight = shopConfig.getProperty(Integer.class, "deckBoxHeight");
        exitWidth = shopConfig.getProperty(Integer.class, "exitWidth");
        exitHeight = shopConfig.getProperty(Integer.class, "exitHeight");
        exitSpace = shopConfig.getProperty(Integer.class, "exitSpace");
    }

    public void setDeckBoxList(List<DeckOverview> deckOverviewList){
        deckBox.setDeckList(deckOverviewList.subList(0,deckBoxHeight*deckBoxWidth));
    }
}
