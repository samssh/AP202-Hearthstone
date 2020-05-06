package configs;

import java.io.File;

public class ConfigFactory {
    private static String[] args;
    private static ConfigFactory instance;
    private final static String defaultAddress = "./src/main/resources/configurations/MainConfig.properties";
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Config mainConfig;

    public static ConfigFactory getInstance() {
        if (null == instance) {
            String mainConfigAddress;
            if (args.length > 0) mainConfigAddress = args[0];
            else mainConfigAddress = defaultAddress;
            instance = new ConfigFactory(mainConfigAddress);
        }
        return instance;
    }

    public static void setArgs(String[] args) {
        ConfigFactory.args = args;
    }

    private ConfigFactory(String mainConfigAddress) {
        mainConfig = new Config(mainConfigAddress);
//        mainConfig.put("", "");
    }

    public Config getConfig(String configName) {
        return mainConfig.getProperty(Config.class, configName);
    }

    public File getConfigFile(String configName) {
        return mainConfig.getProperty(File.class, configName);
    }
}
