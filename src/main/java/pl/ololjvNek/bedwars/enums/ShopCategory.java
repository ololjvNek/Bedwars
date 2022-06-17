package pl.ololjvNek.bedwars.enums;

import lombok.Getter;
import org.bukkit.Material;
import pl.ololjvNek.bedwars.utils.Lang;

public enum ShopCategory {

    BUILD(Lang.SHOP_CATEGORY_BUILD, Material.COBBLESTONE),
    ARMOR(Lang.SHOP_CATEGORY_ARMOR, Material.DIAMOND_CHESTPLATE),
    TOOLS(Lang.SHOP_CATEGORY_TOOLS, Material.IRON_AXE),
    WEAPONS(Lang.SHOP_CATEGORY_WEAPONS, Material.DIAMOND_SWORD),
    POTIONS(Lang.SHOP_CATEGORY_POTIONS, Material.BREWING_STAND_ITEM),
    OTHER(Lang.SHOP_CATEGORY_OTHER, Material.NETHER_STAR);

    @Getter private String name;
    @Getter private Material guiItem;

    ShopCategory(String n, Material m){
        name = n;
        guiItem = m;
    }
}
