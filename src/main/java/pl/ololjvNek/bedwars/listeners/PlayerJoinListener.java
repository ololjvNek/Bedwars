package pl.ololjvNek.bedwars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.managers.UserManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        e.setJoinMessage(null);
        if(UserManager.getUser(p) == null){
            UserManager.createUser(p);
        }
        if(Main.getBedwarsGame().getGameStatus().equals("STARTED")){
            Main.getBedwarsGame().joinAsSpectator(p);
        }else{
            Main.getBedwarsGame().joinToGame(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Main.getBedwarsGame().leaveGame(e.getPlayer());
    }
}
