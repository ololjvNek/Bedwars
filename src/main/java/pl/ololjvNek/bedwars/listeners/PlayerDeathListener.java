package pl.ololjvNek.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(!(e.getEntity() instanceof Player)){
            if(Main.getEventManager().getStartedEventType() != null){
                if(e.getEntity() instanceof Zombie){
                    Main.getEventManager().getZombieList().remove((Zombie)e.getEntity());
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        e.setDeathMessage(null);
        if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player){
            final Player killer = e.getEntity().getKiller();
            final Player death = e.getEntity();
            final User uK = UserManager.getUser(killer);
            final User uD = UserManager.getUser(death);

            List<ItemStack> toDrop = new ArrayList<>();
            for(ItemStack is : e.getDrops()){
                if(is != null && (is.getType() == Material.DIAMOND || is.getType() == Material.EMERALD || is.getType() == Material.IRON_INGOT || is.getType() == Material.GOLD_INGOT)){
                    toDrop.add(is);
                }
            }
            e.getDrops().clear();
            Util.dropToEquipment(toDrop, killer);
            if(uD.getTeam().isBedAlive()){
                Util.sendMessage(Main.getBedwarsGame().getPlayers(), Lang.MESSAGES_KILL.replace("{DEATH}", death.getName()).replace("{KILLER}", killer.getName()));
            }else{
                Util.sendMessage(Main.getBedwarsGame().getPlayers(), Lang.MESSAGES_ELIMINATED.replace("{DEATH}", death.getName()).replace("{KILLER}", killer.getName()));
                Util.sendTitle(death, Lang.MESSAGES_YOUDEAD);
                new BukkitRunnable(){
                    public void run(){
                        if(death.isDead()){
                            death.spigot().respawn();
                            Main.getBedwarsGame().joinAsSpectator(death);
                        }
                    }
                }.runTaskLater(Main.getPlugin(), 5L);
                return;
            }

            new BukkitRunnable(){
                public void run(){
                    if(death.isDead()){
                        death.spigot().respawn();
                        uD.getTeam().respawnPlayer(death);
                    }
                }
            }.runTaskLater(Main.getPlugin(), 5L);
        }else if(e.getEntity() instanceof Player){
            final Player death = e.getEntity();
            final User uD = UserManager.getUser(death);

            e.getDrops().clear();
            if(uD.getTeam().isBedAlive()){
                Util.sendMessage(Main.getBedwarsGame().getPlayers(), Lang.MESSAGES_KILL.replace("{DEATH}", death.getName()).replace("{KILLER}", Lang.MESSAGES_OTHERKILLER));
            }else{
                Util.sendMessage(Main.getBedwarsGame().getPlayers(), Lang.MESSAGES_ELIMINATED.replace("{DEATH}", death.getName()).replace("{KILLER}", Lang.MESSAGES_OTHERKILLER));
                Util.sendTitle(death, Lang.MESSAGES_YOUDEAD);
                new BukkitRunnable(){
                    public void run(){
                        if(death.isDead()){
                            death.spigot().respawn();
                            Main.getBedwarsGame().joinAsSpectator(death);
                        }
                    }
                }.runTaskLater(Main.getPlugin(), 5L);
                return;
            }

            new BukkitRunnable(){
                public void run(){
                    if(death.isDead()){
                        death.spigot().respawn();
                        uD.getTeam().respawnPlayer(death);
                    }
                }
            }.runTaskLater(Main.getPlugin(), 5L);
        }
    }
}
