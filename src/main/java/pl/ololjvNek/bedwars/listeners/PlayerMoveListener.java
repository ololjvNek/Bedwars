package pl.ololjvNek.bedwars.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.managers.UserManager;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        final Player p = e.getPlayer();
        if(Main.getBedwarsGame().getGameStatus().equals("STARTED")){
            if(Main.getBedwarsGame().getPlayers().contains(p)){
                if(p.getLocation().getY() < 25){
                    if(p.getGameMode() == GameMode.ADVENTURE && p.getAllowFlight()){
                        p.teleport(Main.getBedwarsGame().getLobby().clone().subtract(0, 7, 0));
                        if(!p.isFlying()){
                            p.setFlying(true);
                        }
                        return;
                    }
                    p.damage(20000);
                }
            }else{
                if(p.getLocation().getY() < 25 && p.getGameMode() == GameMode.ADVENTURE){
                    p.teleport(Main.getBedwarsGame().getLobby().clone().subtract(0, 7, 0));
                    if(!p.isFlying()){
                        p.setFlying(true);
                    }
                }
            }
        }
    }
}
