package ir.sam.hearthstone.resource_manager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigFactory {
    private static ConfigFactory instance;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Config mainConfig;

    public static ConfigFactory getInstance() {
        if (null == instance) {
            instance = new ConfigFactory(ResourceLoader.getConfigAddress());
        }
        return instance;
    }

    private ConfigFactory(String mainConfigAddress) {
        mainConfig = new Config(mainConfigAddress);
        setFonts(mainConfig.getProperty("FONT"));
    }

    private void setFonts(String address){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            File file = new File(address);
            File[] files = file.listFiles();
            for (File value : Objects.requireNonNull(files)) {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, value));
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig(String configName) {
        return mainConfig.getProperty(Config.class, configName);
    }

    public File getConfigFile(String configName) {
        return mainConfig.getProperty(File.class, configName);
    }
}
