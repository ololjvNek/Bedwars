package pl.ololjvNek.bedwars.data;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.enums.ShopCategory;
import pl.ololjvNek.bedwars.utils.ItemBuilder;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.HashMap;
import java.util.Map;

@Data
public class Shop {

    private FileConfiguration storage = Main.getShopStorage().getConfig();

    private String shopName;
    private ShopCategory category;
    private Material guiItem;
    private int guiAmount;
    private byte guiData;
    private String guiName;
    private ItemStack cost;
    private Map<Enchantment, Integer> enchantments;

    public Shop(String shopName){
        this.shopName = shopName;
        this.category = ShopCategory.valueOf(getStorage().getString("shop." + shopName + ".category"));
        this.guiItem = Material.getMaterial(getStorage().getString("shop." + shopName + ".guiItem"));
        this.guiAmount = getStorage().getInt("shop." + shopName + ".guiAmount");
        this.guiData = Byte.parseByte("" + getStorage().getInt("shop." + shopName + ".guiData"));
        this.guiName = getStorage().getString("shop." + shopName + ".guiName");
        this.cost = Util.parseStringToItemStack(getStorage().getString("shop." + shopName + ".cost"));
        if(getStorage().getString("shop." + shopName + ".enchantments") != null){
            this.enchantments = Util.getEnchantmentsFromString(getStorage().getString("shop." + shopName + ".enchantments"));
        }else{
            this.enchantments = new HashMap<>();
        }
    }

    public ItemStack getItem(){
        return new ItemBuilder(getGuiItem(), getGuiAmount(), getGuiData()).addUnsafeEnchantments(getEnchantments()).toItemStack();
    }
}
