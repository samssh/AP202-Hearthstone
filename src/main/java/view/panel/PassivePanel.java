package view.panel;

import client.Client;
import configs.Config;
import configs.ConfigFactory;
import view.model.PassiveOverview;
import view.util.PassiveBox;

import javax.swing.*;
import java.util.List;

public class PassivePanel extends JPanel {
    private PassiveBox passiveBox;
    private final Client.PassiveAction passiveAction;
    private JButton exit, back, backMainMenu;
    private int exitX, exitY, exitWidth, exitHeight, exitSpace;
    private int x, y, width, height;
    private int passiveBoxX, passiveBoxY, passiveBoxWidth, passiveBoxHeight;

    public PassivePanel(Client.PassiveAction passiveAction) {
        setLayout(null);
        this.passiveAction = passiveAction;
        config();
        this.setBounds(x, y, width, height);
        initialize();
        this.add(passiveBox);
        this.add(exit);
        this.add(back);
        this.add(backMainMenu);
    }

    private void initialize() {
        initializePassiveBox();
        initializeBack();
        initializeExit();
        initializeBackMainMenu();
    }

    private void initializePassiveBox() {
        passiveBox = new PassiveBox(passiveBoxWidth, passiveBoxHeight,this,passiveAction::selectPassive);
        passiveBox.setLocation(passiveBoxX, passiveBoxY);
        passiveBox.setTitle("choose one:");
    }



    private void initializeExit(){
        exit = new JButton("exit");
        exit.setBounds(exitX, exitY, exitWidth, exitHeight);
        exit.addActionListener(e -> passiveAction.exit());
    }

    private void initializeBack(){
        back = new JButton("back");
        int x = exitX - 2 * (exitWidth + exitSpace);
        back.setBounds(x, exitY, exitWidth, exitHeight);
        back.addActionListener(e -> passiveAction.back());
    }

    private void initializeBackMainMenu(){
        backMainMenu = new JButton("back to main menu");
        int x = exitX - (exitWidth + exitSpace);
        backMainMenu.setBounds(x, exitY, exitWidth, exitHeight);
        backMainMenu.addActionListener(e -> passiveAction.backMainMenu());
    }

    public void setPassives(List<PassiveOverview> passives){
        passiveBox.setModels(passives);
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
    }
}