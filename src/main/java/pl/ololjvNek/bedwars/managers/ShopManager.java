package pl.ololjvNek.bedwars.managers;

import lombok.Getter;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.comparators.ShopComparator;
import pl.ololjvNek.bedwars.data.Shop;
import pl.ololjvNek.bedwars.enums.ShopCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShopManager {

    @Getter private static HashMap<String, Shop> shops = new HashMap<>();

    public static Shop getShop(String name){
        return shops.get(name);
    }

    public static void loadShops(){
        if(Main.getShopStorage().getConfig().getConfigurationSection("shop") == null){
            return;
        }
        for(final String s : Main.getShopStorage().getConfig().getConfigurationSection("shop").getKeys(false)){
            final Shop shop = new Shop(s);
            shops.put(shop.getShopName(), shop);
        }

    }

    public static List<Shop> getShopsWithCategory(ShopCategory category){
        final List<Shop> shops = new ArrayList<>(getShops().values());
        final List<Shop> toReturn = new ArrayList<>();
        for(Shop shop : shops){
            if(shop.getCategory() == category){
                toReturn.add(shop);
            }
        }
        toReturn.sort(new ShopComparator());
        return toReturn;
    }
}
