package configs;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigFactory {
    private static ConfigFactory instance;
    private final static String defaultAddress="./src/main/resources/configurations/MainConfig.properties";
    private final AtomicReference<Config> mainConfig = new AtomicReference<>();

    public static ConfigFactory getInstance(String mainConfigAddress) {
        if (null == instance){
            instance=new ConfigFactory();
            if(mainConfigAddress.equals("DEFAULT"))
                mainConfigAddress=defaultAddress;
            instance.mainConfig.set(new Config(mainConfigAddress));
        }
        return instance;
    }

    public Config getConfig(String configName){
        return mainConfig.get().getProperty(Config.class,configName);
    }

    public File getConfigFile(String configName){
        return mainConfig.get().getProperty(File.class,configName);
    }
}
