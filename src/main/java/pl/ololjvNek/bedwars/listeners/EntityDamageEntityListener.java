package pl.ololjvNek.bedwars.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.ololjvNek.bedwars.Main;

import java.util.concurrent.ThreadLocalRandom;

public class EntityDamageEntityListener implements Listener {

    @EventHandler
    public void event(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player){
                final Player damager = (Player) e.getDamager();
                final Player victim = (Player) e.getEntity();
                if(!Main.getBedwarsGame().getGameStatus().equals("STARTED")){
                    e.setCancelled(true);
                }
                if(Main.getBedwarsGame().getSpectators().contains(damager)){
                    e.setCancelled(true);
                }
            }
        }else if(e.getEntity() instanceof EnderDragon){
            if(e.getDamager() instanceof Player){
                final Player player = (Player) e.getDamager();
                double chance = (Math.random()*100);
                if(chance < 50){

                }
            }
        }
    }


    @EventHandler
    public void damage(EntityDamageEvent e){
        if(!Main.getBedwarsGame().getGameStatus().equals("STARTED")){
            e.setCancelled(true);
        }
    }
}
