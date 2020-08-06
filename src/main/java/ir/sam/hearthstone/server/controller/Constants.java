package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.resource_loader.Config;
import ir.sam.hearthstone.server.resource_loader.ConfigFactory;

public class Constants {
    public final static int STARTING_MANA;
    public final static int MANA_PER_TURN;
    public final static int CARD_PER_TURN;
    public final static int MAX_DECK_SIZE;
    public final static int STARTING_PASSIVES;
    public final static int STARTING_HAND_CARDS;
    public final static int MAX_MANA;
    public final static int STARTING_COINS;
    public final static int MAX_DECK_NUMBER;
    public final static int TURN_TIME;// to millisecond
    public final static int MAX_HAND_SIZE;
    public final static int MAX_GROUND_SIZE;

    static {
        Config config = ConfigFactory.getInstance().getConfig("SERVER_CONFIG");
        STARTING_MANA = config.getProperty(Integer.class, "STARTING_MANA");
        MANA_PER_TURN = config.getProperty(Integer.class, "MANA_PER_TURN");
        CARD_PER_TURN = config.getProperty(Integer.class, "CARD_PER_TURN");
        MAX_DECK_SIZE = config.getProperty(Integer.class, "MAX_DECK_SIZE");
        STARTING_PASSIVES = config.getProperty(Integer.class, "STARTING_PASSIVES");
        STARTING_HAND_CARDS = config.getProperty(Integer.class, "STARTING_HAND_CARDS");
        MAX_MANA = config.getProperty(Integer.class, "MAX_MANA");
        STARTING_COINS = config.getProperty(Integer.class, "STARTING_COINS");
        MAX_DECK_NUMBER = config.getProperty(Integer.class, "MAX_DECK_NUMBER");
        TURN_TIME = config.getProperty(Integer.class, "TURN_TIME");
        MAX_HAND_SIZE = config.getProperty(Integer.class, "MAX_HAND_SIZE");
        MAX_GROUND_SIZE = config.getProperty(Integer.class, "MAX_GROUND_SIZE");
    }
}
