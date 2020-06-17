package ir.sam.hearthstone.resource_manager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final ImageLoader instance = new ImageLoader();
    private final Map<String, BufferedImage> big, bigGray, smallGray, small, bg, bigDeck, smallDeck,
            passive,smallHero,bigHero,smallHeroPower,bigHeroPower;

    private ImageLoader() {
        Config bigConfig = ConfigFactory.getInstance().getConfig("BIG_CARDS");
        big = new HashMap<>();
        load(bigConfig, big);
        Config smallConfig = ConfigFactory.getInstance().getConfig("SMALL_CARDS");
        small = new HashMap<>();
        load(smallConfig, small);
        Config bgConfig = ConfigFactory.getInstance().getConfig("BG");
        bg = new HashMap<>();
        load(bgConfig, bg);
        Config bigGrayConfig = ConfigFactory.getInstance().getConfig("BIG_GRAY_CARDS");
        bigGray = new HashMap<>();
        load(bigGrayConfig, bigGray);
        Config smallGayConfig = ConfigFactory.getInstance().getConfig("SMALL_GRAY_CARDS");
        smallGray = new HashMap<>();
        load(smallGayConfig, smallGray);
        Config bigDeckConfig = ConfigFactory.getInstance().getConfig("BIG_DECK");
        bigDeck = new HashMap<>();
        load(bigDeckConfig, bigDeck);
        Config smallDeckConfig = ConfigFactory.getInstance().getConfig("SMALL_DECK");
        smallDeck = new HashMap<>();
        load(smallDeckConfig, smallDeck);
        Config passiveDeckConfig = ConfigFactory.getInstance().getConfig("PASSIVE");
        passive = new HashMap<>();
        load(passiveDeckConfig, passive);
        Config bigHeroConfig = ConfigFactory.getInstance().getConfig("BIG_HEROES");
        bigHero = new HashMap<>();
        load(bigHeroConfig, bigHero);
        Config smallHeroConfig = ConfigFactory.getInstance().getConfig("SMALL_HEROES");
        smallHero = new HashMap<>();
        load(smallHeroConfig,smallHero);
        Config bigHeroPowerConfig = ConfigFactory.getInstance().getConfig("BIG_HERO_POWERS");
        bigHeroPower = new HashMap<>();
        load(bigHeroPowerConfig, bigHeroPower);
        Config smallHeroPowerConfig = ConfigFactory.getInstance().getConfig("SMALL_HERO_POWERS");
        smallHeroPower = new HashMap<>();
        load(smallHeroPowerConfig,smallHeroPower);
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
                System.out.println(file.toString());
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

    public BufferedImage getBigDeck(String heroName) {
        return bigDeck.get(heroName);
    }

    public BufferedImage getSmallDeck(String heroName) {
        return smallDeck.get(heroName);
    }

    public BufferedImage getPassive(String name) {
        return passive.get(name);
    }

    public BufferedImage getSmallHero(String name) {
        return smallHero.get(name);
    }

    public BufferedImage getBigHero(String name) {
        return bigHero.get(name);
    }

    public BufferedImage getSmallHeroPower(String name) {
        return smallHeroPower.get(name);
    }

    public BufferedImage getBigHeroPower(String name) {
        return bigHeroPower.get(name);
    }
}

