package pl.ololjvNek.bedwars.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Team;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.enums.UpgradeType;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.ItemBuilder;
import pl.ololjvNek.bedwars.utils.ItemUtil;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class UpgradeProvider implements InventoryProvider {

    public static SmartInventory INVENTORY = SmartInventory.builder().manager(Main.getInventoryManager()).provider(new UpgradeProvider()).title(Util.fixColors("&8            * &9ULEPSZENIA &8*")).size(5, 9).build();


    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        final User u = UserManager.getUser(player);
        int row = 2;
        int slot = 2;
        if(u.getTeam() != null){
            Team team = u.getTeam();
            for(UpgradeType upgradeType : UpgradeType.values()){
                ItemStack upgradeCost = team.getCostToUpgrade(upgradeType);
                String guiName = upgradeType.getGuiName().replace("{LEVEL}", String.valueOf((team.getLevelFromUpgradeType(upgradeType) >= upgradeType.getMaxLevel() ? Lang.MESSAGES_MAXEDLEVEL : team.getLevelFromUpgradeType(upgradeType)))).replace("{LEVELUP}", String.valueOf((team.getLevelFromUpgradeType(upgradeType) >= upgradeType.getMaxLevel() ? Lang.MESSAGES_MAXEDLEVEL : team.getLevelFromUpgradeType(upgradeType)+1)).replace("{COST}", Util.replaceItemStackToString(upgradeCost)));
                List<String> guiLore = new ArrayList<>();
                for(String s : upgradeType.getGuiLore()){
                    guiLore.add(Util.fixColors(s.replace("{LEVEL}", String.valueOf((team.getLevelFromUpgradeType(upgradeType) >= upgradeType.getMaxLevel() ? Lang.MESSAGES_MAXEDLEVEL : team.getLevelFromUpgradeType(upgradeType)))).replace("{LEVELUP}", String.valueOf((team.getLevelFromUpgradeType(upgradeType) >= upgradeType.getMaxLevel() ? Lang.MESSAGES_MAXEDLEVEL : team.getLevelFromUpgradeType(upgradeType)+1))).replace("{COST}", Util.replaceItemStackToString(upgradeCost))));
                }
                ItemBuilder ib = new ItemBuilder(upgradeType.getGuiItem(), 1).setName(Util.fixColors(guiName)).setLore(Util.fixColors(guiLore));
                inventoryContents.set(row, slot, ClickableItem.of(ib.toItemStack(), e->{
                    if(team.getLevelFromUpgradeType(upgradeType) >= upgradeType.getMaxLevel()){
                        return;
                    }
                    if(ItemUtil.playerHasItemAndRemove(player, upgradeCost)){
                        team.addLevelToUpgradeType(upgradeType, 1);
                        player.closeInventory();
                        INVENTORY.open(player);
                        Util.sendMessage(player, Lang.MESSAGES_SUCCESSBUY);
                    }else{
                        player.closeInventory();
                        Util.sendMessage(player, Lang.MESSAGES_CANTAFFORD);
                    }
                }));

                slot++;
            }
        }
    }


    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
