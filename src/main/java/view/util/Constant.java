package view.util;

import resourceManager.Config;
import resourceManager.ConfigFactory;

import javax.swing.*;
import java.awt.*;

public class Constant {
    public static int CARD_WIDTH;
    public static int CARD_HEIGHT;
    public static int CARD_SPACE;
    public static int BOX_BUTTON_WIDTH;
    public static int BOX_BUTTON_HEIGHT;
    public static int BOX_LABEL_HEIGHT;
    public static int BIG_DECK_WIDTH;
    public static int BIG_DECK_HEIGHT;
    public static int BIG_DECK_SPACE;
    public static int SMALL_DECK_WIDTH;
    public static int SMALL_DECK_HEIGHT;
    public static int SMALL_DECK_SPACE;
    public static int PASSIVE_WIDTH;
    public static int PASSIVE_HEIGHT;
    public static int PASSIVE_SPACE;
    public static int HERO_WIDTH;
    public static int HERO_HEIGHT;
    public static int HERO_POWER_WIDTH;
    public static int HERO_POWER_HEIGHT;

    static {
        Config config = ConfigFactory.getInstance().getConfig("CONSTANT_CONFIG");
        CARD_WIDTH = config.getProperty(Integer.class, "CARD_WIDTH");
        CARD_HEIGHT = config.getProperty(Integer.class, "CARD_HEIGHT");
        CARD_SPACE = config.getProperty(Integer.class, "CARD_SPACE");
        BOX_BUTTON_WIDTH = config.getProperty(Integer.class, "BOX_BUTTON_WIDTH");
        BOX_BUTTON_HEIGHT = config.getProperty(Integer.class, "BOX_BUTTON_HEIGHT");
        BOX_LABEL_HEIGHT = config.getProperty(Integer.class, "BOX_LABEL_HEIGHT");
        BIG_DECK_WIDTH = config.getProperty(Integer.class, "BIG_DECK_WIDTH");
        BIG_DECK_HEIGHT = config.getProperty(Integer.class, "BIG_DECK_HEIGHT");
        BIG_DECK_SPACE = config.getProperty(Integer.class, "BIG_DECK_SPACE");
        SMALL_DECK_WIDTH = config.getProperty(Integer.class, "SMALL_DECK_WIDTH");
        SMALL_DECK_HEIGHT = config.getProperty(Integer.class, "SMALL_DECK_HEIGHT");
        SMALL_DECK_SPACE = config.getProperty(Integer.class, "SMALL_DECK_SPACE");
        PASSIVE_WIDTH = config.getProperty(Integer.class, "PASSIVE_WIDTH");
        PASSIVE_HEIGHT = config.getProperty(Integer.class, "PASSIVE_HEIGHT");
        PASSIVE_SPACE = config.getProperty(Integer.class, "PASSIVE_SPACE");
        HERO_WIDTH = config.getProperty(Integer.class, "HERO_WIDTH");
        HERO_HEIGHT = config.getProperty(Integer.class, "HERO_HEIGHT");
        HERO_POWER_WIDTH = config.getProperty(Integer.class, "HERO_POWER_WIDTH");
        HERO_POWER_HEIGHT = config.getProperty(Integer.class, "HERO_POWER_HEIGHT");
    }

    public static void makeTransparent(JButton button){
        makeWhite(button);
        button.setFocusable(false);
        button.setBorderPainted(false);
    }

    public static void makeWhite(JComponent component){
        component.setFont(component.getFont().deriveFont(18.F));
        component.setForeground(Color.WHITE);
        component.setBackground(new Color(10,10,10,200));
    }
}
