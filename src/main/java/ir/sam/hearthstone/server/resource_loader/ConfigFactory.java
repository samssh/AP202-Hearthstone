package ir.sam.hearthstone.server.resource_loader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigFactory {
    private final static String defaultAddress = "./src/main/resources/configurations/MainConfig.properties";
    private static String[] args;
    private static ConfigFactory instance;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Config mainConfig;

    public static ConfigFactory getInstance() {
        if (null == instance) {
            instance = new ConfigFactory(getConfigAddress());
        }
        return instance;
    }

    private ConfigFactory(String mainConfigAddress) {
        mainConfig = new Config(mainConfigAddress);
    }

    public Config getConfig(String configName) {
        return mainConfig.getProperty(Config.class, configName);
    }

    public File getConfigFile(String configName) {
        return mainConfig.getProperty(File.class, configName);
    }

    public static String getConfigAddress() {
        return args.length > 0 ? args[0] : defaultAddress;
    }

    public static void setArgs(String[] args) {
        ConfigFactory.args = args;
    }
}
