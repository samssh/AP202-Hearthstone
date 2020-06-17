package ir.sam.hearthstone.view.panel;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.resource_manager.Config;
import ir.sam.hearthstone.resource_manager.ConfigFactory;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.util.Updatable;
import ir.sam.hearthstone.view.model.BigDeckOverview;
import ir.sam.hearthstone.view.util.BigDeckBox;
import ir.sam.hearthstone.view.util.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class StatusPanel extends JPanel implements Updatable {
    private BigDeckBox bigDeckBox;
    private JButton exit, back, backMainMenu;
    private final BufferedImage image;
    private int deckBoxX,deckBoxY,deckBoxWidth,deckBoxHeight;
    private int exitX,exitY,exitWidth,exitHeight,exitSpace;
    private final Client.StatusAction statusAction;

    public StatusPanel(Client.StatusAction statusAction) {
        setLayout(null);
        this.statusAction = statusAction;
        this.image = ImageLoader.getInstance().getBackground("status");
        config();
        initialize();
        this.add(bigDeckBox);
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
        bigDeckBox = new BigDeckBox(deckBoxWidth,deckBoxHeight,this,null);
        bigDeckBox.setLocation(deckBoxX,deckBoxY);
        bigDeckBox.setTitle("your best deck");
    }

    private void initializeExit(){
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> statusAction.exit());
        Constant.makeTransparent(exit);
    }

    private void initializeBack(){
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(e -> statusAction.back());
        Constant.makeTransparent(back);
    }

    private void initializeBackMainMenu(){
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(e -> statusAction.backMainMenu());
        Constant.makeTransparent(backMainMenu);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
    }

    private void config() {
        Config shopConfig = ConfigFactory.getInstance().getConfig("STATUS_CONFIG");
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

    public void setDeckBoxList(List<BigDeckOverview> bigDeckOverviewList){
        bigDeckBox.setModels(bigDeckOverviewList.subList(0,Math.min(deckBoxHeight*deckBoxWidth, bigDeckOverviewList.size())));
    }

    @Override
    public void update() {
        statusAction.update();
    }
}
