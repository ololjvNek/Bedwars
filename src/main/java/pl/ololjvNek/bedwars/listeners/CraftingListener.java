package pl.ololjvNek.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.managers.UserManager;

public class CraftingListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        final Player p = e.getPlayer();
        if(Main.getBedwarsGame().getSpectators().contains(p)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerDropItemEvent e){
        final Player p = e.getPlayer();
        if(Main.getBedwarsGame().getSpectators().contains(p)){
            e.setCancelled(true);
        }
    }
}
