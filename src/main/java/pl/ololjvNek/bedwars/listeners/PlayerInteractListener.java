package pl.ololjvNek.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.providers.ChooseTeamProvider;
import pl.ololjvNek.bedwars.providers.ShopProvider;
import pl.ololjvNek.bedwars.providers.UpgradeProvider;
import pl.ololjvNek.bedwars.utils.ItemUtil;
import pl.ololjvNek.bedwars.utils.Util;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        final Player p = e.getPlayer();
        if(p.getItemInHand() != null && p.getItemInHand().isSimilar(ItemUtil.getChooseTeam().toItemStack())){
            ChooseTeamProvider.INVENTORY.open(p);
        }else if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.FIREBALL){
            Fireball fireball = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREBALL);
            fireball.setDirection(p.getEyeLocation().getDirection().multiply(2.0D));
            p.getInventory().removeItem(new ItemStack(Material.FIREBALL, 1));
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e){
        final Player p = e.getPlayer();
        if(e.getRightClicked().getType() == EntityType.VILLAGER && e.getRightClicked().getCustomName().equals(Util.fixColors(Main.getGlobalConfig().getConfig().getString("settings.shops.villagername")))){
            e.setCancelled(true);
            ShopProvider.INVENTORY.open(p);
        }else if(e.getRightClicked().getType() == EntityType.VILLAGER && e.getRightClicked().getCustomName().equals(Util.fixColors(Main.getGlobalConfig().getConfig().getString("settings.upgrade.villagername")))){
            e.setCancelled(true);
            UpgradeProvider.INVENTORY.open(p);
        }
    }
}
