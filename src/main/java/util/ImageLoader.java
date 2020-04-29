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
    private final Map<String, BufferedImage> big, bigGray, smallGray, small, bg , bigDeck,smallDeck;

    private ImageLoader() {
        Config bigConfig = ConfigFactory.getInstance("").getConfig("BIG_CARDS");
        big = new HashMap<>();
        load(bigConfig, big);
        Config smallConfig = ConfigFactory.getInstance("").getConfig("SMALL_CARDS");
        small = new HashMap<>();
        load(smallConfig, small);
        Config bgConfig = ConfigFactory.getInstance("").getConfig("BG");
        bg = new HashMap<>();
        load(bgConfig, bg);
        Config bigGrayConfig = ConfigFactory.getInstance("").getConfig("BIG_GRAY_CARDS");
        bigGray = new HashMap<>();
        load(bigGrayConfig, bigGray);
        Config smallGayConfig = ConfigFactory.getInstance("").getConfig("SMALL_GRAY_CARDS");
        smallGray = new HashMap<>();
        load(smallGayConfig, smallGray);
        Config bigDeckConfig = ConfigFactory.getInstance("").getConfig("BIG_DECK");
        bigDeck = new HashMap<>();
        load(bigDeckConfig, bigDeck);
        Config smallDeckConfig = ConfigFactory.getInstance("").getConfig("SMALL_DECK");
        smallDeck = new HashMap<>();
        load(smallDeckConfig, smallDeck);
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

    public BufferedImage getBigDeck(String heroName){
        return bigDeck.get(heroName);
    }

    public BufferedImage getSmallDeck(String heroName){
        return smallDeck.get(heroName);
    }
}

