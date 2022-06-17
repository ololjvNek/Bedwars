package pl.ololjvNek.bedwars.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import pl.ololjvNek.bedwars.Main;


public class ExplosionListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if(e.getEntityType() == EntityType.PRIMED_TNT){
            e.blockList().removeIf(block -> !Main.getBedwarsGame().getBlocksPlaced().contains(block.getLocation()));
        }else{
            e.blockList().clear();
        }
    }
}
