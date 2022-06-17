package pl.ololjvNek.bedwars.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.managers.EventManager;
import pl.ololjvNek.bedwars.managers.UserManager;

public class MobSpawningListener implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getEntity().getWorld().getName().equals("bedwars") && e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(Main.getEventManager().getStartedEventType() == null){
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e){
        if(e.getEntity() instanceof EnderDragon){
            if(e.getTarget() instanceof Player){
                final Player target = (Player) e.getTarget();
                if(Main.getBedwarsGame().getSpectators().contains(target)){
                    e.setCancelled(true);
                }
            }
        }
    }
}
