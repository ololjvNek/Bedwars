package pl.ololjvNek.bedwars.enums;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.utils.Lang;

import java.util.List;

public enum UpgradeType {

    ARMOR(Lang.UPGRADE_ARMOR_GUINAME, Lang.UPGRADE_ARMOR_GUILORE, Material.getMaterial(Main.getGlobalConfig().getConfig().getString("settings.upgrade.armor.guiItem")), 4),
    WEAPON(Lang.UPGRADE_WEAPON_GUINAME, Lang.UPGRADE_WEAPON_GUILORE, Material.getMaterial(Main.getGlobalConfig().getConfig().getString("settings.upgrade.weapon.guiItem")), 1),
    FURNACE(Lang.UPGRADE_FURNACE_GUINAME, Lang.UPGRADE_FURNACE_GUILORE, Material.getMaterial(Main.getGlobalConfig().getConfig().getString("settings.upgrade.furnace.guiItem")), 4),
    SPEED(Lang.UPGRADE_SPEED_GUINAME, Lang.UPGRADE_SPEED_GUILORE, Material.getMaterial(Main.getGlobalConfig().getConfig().getString("settings.upgrade.speed.guiItem")), 2),
    BED(Lang.UPGRADE_BED_GUINAME, Lang.UPGRADE_BED_GUILORE, Material.getMaterial(Main.getGlobalConfig().getConfig().getString("settings.upgrade.bed.guiItem")), 2);

    @Getter private final String guiName;
    @Getter private final List<String> guiLore;
    @Getter private final Material guiItem;
    @Getter private final int maxLevel;


    UpgradeType(String guiName, List<String> guiLore, Material material, int maxLevel){
        this.guiName = guiName;
        this.guiLore = guiLore;
        this.guiItem = material;
        this.maxLevel = maxLevel;
    }
}
