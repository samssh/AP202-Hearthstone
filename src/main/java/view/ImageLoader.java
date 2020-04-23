package view;

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
    private final Map<String, BufferedImage> big, small, bg;

    private ImageLoader() {
        Config smallConfig = ConfigFactory.getInstance("").getConfig("SMALL_CARDS");
        Config bigConfig = ConfigFactory.getInstance("").getConfig("BIG_CARDS");
        Config bgConfig = ConfigFactory.getInstance("").getConfig("BG");
        big = new HashMap<>();
        small = new HashMap<>();
        bg = new HashMap<>();
        load(smallConfig, small);
        load(bigConfig, big);
        load(bgConfig, bg);
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
}

