package pl.ololjvNek.bedwars.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.configs.ConfigCreator;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class Lang {

    private static FileConfiguration lang;

    public static String PREFIX_RED, PREFIX_BLUE, PREFIX_YELLOW, PREFIX_GREEN, PREFIX_PURPLE, PREFIX_GRAY, PREFIX_AQUA, PREFIX_WHITE,
            GENERATORS_IRON, GENERATORS_GOLD, GENERATORS_DIAMOND, GENERATORS_EMERALD,
            SHOP_CATEGORY_BUILD, SHOP_CATEGORY_ARMOR, SHOP_CATEGORY_TOOLS, SHOP_CATEGORY_WEAPONS, SHOP_CATEGORY_POTIONS, SHOP_CATEGORY_OTHER,
            UPGRADE_ARMOR_GUINAME, UPGRADE_WEAPON_GUINAME, UPGRADE_FURNACE_GUINAME, UPGRADE_SPEED_GUINAME, UPGRADE_BED_GUINAME,
            EVENT_PREFIX_DRAGON, EVENT_PREFIX_ZOMBIE, EVENT_BOARD_NEXTEVENT, EVENT_BOARD_EVENTUNDERWAY, EVENT_MESSAGES_STARTEVENT,
            MESSAGES_JOINTOGAME, MESSAGES_DESTROY, MESSAGES_PLACE, MESSAGES_DESTROYBED, MESSAGES_DESTROYEDBED, MESSAGES_MAXEDLEVEL, MESSAGES_RESPAWNIN,
            MESSAGES_ELIMINATED, MESSAGES_KILL, MESSAGES_YOUDEAD, MESSAGES_OTHERKILLER, MESSAGES_TEAMWIN, MESSAGES_RESTARTING, MESSAGES_CANTAFFORD, MESSAGES_SUCCESSBUY;

    public static List<String> UPGRADE_ARMOR_GUILORE, UPGRADE_WEAPON_GUILORE, UPGRADE_FURNACE_GUILORE, UPGRADE_SPEED_GUILORE, UPGRADE_BED_GUILORE;


    public Lang(String langType){
        File file = new File(Main.getPlugin().getDataFolder() + "/langs", langType.toLowerCase() + ".yml");
        if(file.exists()){
            lang = YamlConfiguration.loadConfiguration(file);
        }else{
            Main.getPlugin().saveResource("langs/pl.yml", false);
            file = new File(Main.getPlugin().getDataFolder() + "/langs", langType.toLowerCase() + ".yml");
            lang = YamlConfiguration.loadConfiguration(file);
        }
        setFields();
    }

    private static void setFields(){
        for(Field f : Lang.class.getFields()){
            String fName = f.getName();
            fName = fName.replace("_", ".").toLowerCase();
            f.setAccessible(true);
            try {
                f.set(f.getName(), lang.get(fName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
