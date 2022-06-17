package pl.ololjvNek.bedwars;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import pl.ololjvNek.bedwars.commands.BedwarsCommand;
import pl.ololjvNek.bedwars.configs.ConfigCreator;
import pl.ololjvNek.bedwars.data.Bedwars;
import pl.ololjvNek.bedwars.listeners.*;
import pl.ololjvNek.bedwars.managers.EventManager;
import pl.ololjvNek.bedwars.managers.ShopManager;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.mysql.Store;
import pl.ololjvNek.bedwars.mysql.modes.StoreMySQL;
import pl.ololjvNek.bedwars.utils.ItemUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.worldmanager.worlds.WorldManager;

public class Main extends JavaPlugin {

    @Getter private static Main plugin;

    @Getter private static Scoreboard sbBedwars;

    @Getter private static ConfigCreator globalConfig;
    @Getter private static ConfigCreator shopStorage;

    @Getter private static Bedwars bedwarsGame;
    @Getter private static WorldManager worldManager;

    @Getter private static InventoryManager inventoryManager;
    @Getter private static EventManager eventManager;

    @Getter private static Store store;

    public void onEnable(){
        plugin = this;
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
        store = new StoreMySQL("mysql.titanaxe.com", 3306, "srv220043", "yAXFNfyq", "srv220043", "");
        if(store.connect()){
            store.update("CREATE TABLE IF NOT EXISTS `users`(`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `uuid` text NOT NULL, `lastName` varchar(16) NOT NULL, `coins` int(11) NOT NULL);");
        }
        setupConfigs();
        sbBedwars = Bukkit.getScoreboardManager().getNewScoreboard();
        worldManager = new WorldManager();
        pl.ololjvNek.worldmanager.Main.setPluginAs(plugin);
        bedwarsGame = new Bedwars();
        eventManager = new EventManager();
        setupManagers();
        setupListeners();
        getCommand("bedwars").setExecutor(new BedwarsCommand());
    }

    public void setupConfigs(){
        globalConfig = new ConfigCreator("configuration");
        globalConfig.saveDefaultConfig();
        globalConfig.reloadConfig();
        shopStorage = new ConfigCreator("shopstorage");
        shopStorage.saveDefaultConfig();
        shopStorage.reloadConfig();
        new Lang(getGlobalConfig().getConfig().getString("settings.messages.lang"));
    }

    public void setupManagers(){
        UserManager.loadUsers();
        ShopManager.loadShops();
    }

    public void setupListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageEntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new MobSpawningListener(), this);
        Bukkit.getPluginManager().registerEvents(new ExplosionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new CraftingListener(), this);
    }
}
