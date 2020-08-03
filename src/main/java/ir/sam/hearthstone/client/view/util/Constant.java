package ir.sam.hearthstone.client.view.util;

import ir.sam.hearthstone.client.resource_manager.Config;
import ir.sam.hearthstone.client.resource_manager.ConfigFactory;

import javax.swing.*;
import java.awt.*;

public class Constant {
    public final static int CARD_WIDTH;
    public final static int CARD_HEIGHT;
    public final static int CARD_SPACE;
    public final static int SINGLE_CARD_WIDTH;
    public final static int BOX_BUTTON_WIDTH;
    public final static int BOX_BUTTON_HEIGHT;
    public final static int BOX_LABEL_HEIGHT;
    public final static int BIG_DECK_WIDTH;
    public final static int BIG_DECK_HEIGHT;
    public final static int BIG_DECK_SPACE;
    public final static int SMALL_DECK_WIDTH;
    public final static int SMALL_DECK_HEIGHT;
    public final static int SMALL_DECK_SPACE;
    public final static int PASSIVE_WIDTH;
    public final static int PASSIVE_HEIGHT;
    public final static int PASSIVE_SPACE;
    public final static int HERO_WIDTH;
    public final static int HERO_HEIGHT;
    public final static int HERO_POWER_WIDTH;
    public final static int HERO_POWER_HEIGHT;
    public final static int MINION_WIDTH;
    public final static int MINION_HEIGHT;
    public final static int MINION_SPACE;
    public final static int WEAPON_WIDTH;
    public final static int WEAPON_HEIGHT;

    static {
        Config config = ConfigFactory.getInstance().getConfig("CONSTANT_CONFIG");
        CARD_WIDTH = config.getProperty(Integer.class, "CARD_WIDTH");
        SINGLE_CARD_WIDTH = config.getProperty(Integer.class, "SINGLE_CARD_WIDTH");
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
        MINION_WIDTH = config.getProperty(Integer.class, "MINION_WIDTH");
        MINION_HEIGHT = config.getProperty(Integer.class, "MINION_HEIGHT");
        MINION_SPACE = config.getProperty(Integer.class, "MINION_SPACE");
        WEAPON_WIDTH = config.getProperty(Integer.class, "WEAPON_WIDTH");
        WEAPON_HEIGHT = config.getProperty(Integer.class, "WEAPON_HEIGHT");
    }

    public static void makeTransparent(JButton button) {
        makeWhite(button);
        button.setFocusable(false);
        button.setBorderPainted(false);
    }

    public static void makeWhite(JComponent component) {
        component.setFont(component.getFont().deriveFont(18.F));
        component.setForeground(Color.WHITE);
        component.setBackground(new Color(10, 10, 10, 200));
    }
}
