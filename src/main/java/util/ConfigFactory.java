package util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigFactory {
    private static String[] args;
    private static ConfigFactory instance;
    private final static String defaultAddress = "./src/main/resources/configurations/MainConfig.properties";
    private final static String defaultUrl="http://8upload.ir/uploads/f43519543.zip";
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Config mainConfig;

    public static ConfigFactory getInstance() {
        if (null == instance) {
            instance = new ConfigFactory(getConfigAddress());
        }
        return instance;
    }

    public static String getConfigAddress() {
        return args.length > 0 ? args[0] : defaultAddress;
    }

    public static String getUrl(){
        return args.length > 1 ? args[1] : defaultUrl;
    }

    public static void setArgs(String[] args) {
        ConfigFactory.args = args;
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
