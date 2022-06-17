package pl.ololjvNek.bedwars.configs;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;
import pl.ololjvNek.bedwars.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;

public class ConfigCreator {

    private String fileName;
    @Getter private File file;
    @Getter private FileConfiguration config;

    public ConfigCreator(String fileName){
        this.fileName = fileName;
        file = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
        saveDefaultConfig();
        reloadConfig();
    }

    public FileConfiguration build(){
        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream = Main.getPlugin().getResource(fileName + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig;
            if (!this.isStrictlyUTF8()) {
                defConfig = new YamlConfiguration();

                byte[] contents;
                try {
                    contents = ByteStreams.toByteArray(defConfigStream);
                } catch (IOException var7) {
                    Main.getPlugin().getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", var7);
                    return;
                }

                String text = new String(contents, Charset.defaultCharset());
                if (!text.equals(new String(contents, Charsets.UTF_8))) {
                    Main.getPlugin().getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
                }

                try {
                    defConfig.loadFromString(text);
                } catch (InvalidConfigurationException var6) {
                    Main.getPlugin().getLogger().log(Level.SEVERE, "Cannot load configuration from jar", var6);
                }
            } else {
                defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
            }

            config.setDefaults(defConfig);
        }
    }

    private boolean isStrictlyUTF8() {
        return Main.getPlugin().getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }

    public void saveConfig() {
        try {
            this.getConfig().save(file);
        } catch (IOException var2) {
            Main.getPlugin().getLogger().log(Level.SEVERE, "Could not save config to " + file, var2);
        }

    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            Main.getPlugin().saveResource(fileName + ".yml", false);
        }

    }
}
