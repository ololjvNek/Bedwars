package pl.ololjvNek.bedwars.configs;

import java.util.HashMap;

public class ConfigManager {

    public static HashMap<String, ConfigCreator> configCreatorHashMap = new HashMap<>();

    public static ConfigCreator createConfig(String name){
        ConfigCreator configCreator = new ConfigCreator(name);
        configCreatorHashMap.put(name, configCreator);
        return configCreator;
    }

    public static ConfigCreator getConfigCreator(String name){
        return configCreatorHashMap.get(name);
    }
}
