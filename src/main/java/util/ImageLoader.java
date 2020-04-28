package util;

import configs.Config;
import configs.ConfigFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final ImageLoader instance = new ImageLoader();
    private final Map<String, BufferedImage> big, bigGray, smallGray, small, bg , deck;

    private ImageLoader() {
        Config smallConfig = ConfigFactory.getInstance("").getConfig("SMALL_CARDS");
        Config bigConfig = ConfigFactory.getInstance("").getConfig("BIG_CARDS");
        Config bgConfig = ConfigFactory.getInstance("").getConfig("BG");
        Config bigGrayConfig = ConfigFactory.getInstance("").getConfig("BIG_GRAY_CARDS");
        Config smallGayConfig = ConfigFactory.getInstance("").getConfig("SMALL_GRAY_CARDS");
        Config deckConfig = ConfigFactory.getInstance("").getConfig("DECK_IMAGES");
        big = new HashMap<>();
        load(bigConfig, big);
        small = new HashMap<>();
        load(smallConfig, small);
        bg = new HashMap<>();
        load(bgConfig, bg);
        bigGray = new HashMap<>();
        load(bigGrayConfig, bigGray);
        smallGray = new HashMap<>();
        load(smallGayConfig, smallGray);
        deck = new HashMap<>();
        load(deckConfig,deck);
    }

    public static ImageLoader getInstance() {
        return instance;
    }

    private void load(Config c, Map<String, BufferedImage> m) {
        for (Object k : c.keySet()) {
            String key = (String) k;
            File file = new File(c.getProperty(key));
            try {
                BufferedImage image = ImageIO.read(file);
                String name = key.replace('-', ' ');
                m.put(name, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage getBigCard(String name) {
        return big.get(name);
    }

    public BufferedImage getSmallCard(String name) {
        return small.get(name);
    }

    public BufferedImage getBackground(String name) {
        return bg.get(name);
    }

    public BufferedImage getBigGrayCard(String name) {
        return bigGray.get(name);
    }

    public BufferedImage getSmallGrayCard(String name) {
        return smallGray.get(name);
    }

    public BufferedImage getDeck(String heroName){
        return deck.get(heroName);
    }
}

