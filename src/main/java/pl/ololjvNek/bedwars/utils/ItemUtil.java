package pl.ololjvNek.bedwars.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    @Getter public static ItemBuilder chooseTeam = new ItemBuilder(Material.NETHER_STAR, 1).setName(Util.fixColors("&aWybor druzyny"));
    @Getter public static ItemBuilder teleportToPlayer = new ItemBuilder(Material.COMPASS, 1).setName(Util.fixColors("&aTeleporter"));


    public static boolean playerHasItemAndRemove(Player player, ItemStack itemStack){
        Material searching = itemStack.getType();
        int amountOfItem = 0;
        for(ItemStack is : player.getInventory().getContents()){
            if(is != null && is.getType() == searching){
                amountOfItem += is.getAmount();
            }
        }
        if(amountOfItem >= itemStack.getAmount()){
            player.getInventory().removeItem(itemStack);
            return true;
        }else{
            return false;
        }
    }

}
