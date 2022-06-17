package pl.ololjvNek.bedwars.listeners;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Bedwars;
import pl.ololjvNek.bedwars.data.Team;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.Lang;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.List;

public class BlockBreakPlaceListener implements Listener {

    @Getter private final Bedwars bedwars = Main.getBedwarsGame();
    @Getter private final FileConfiguration config = Main.getGlobalConfig().getConfig();


    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        final Player p = e.getPlayer();
        final User u = UserManager.getUser(p);
        final Team team = u.getTeam();
        if(team.getSpawnLocation().distance(e.getBlockPlaced().getLocation()) < getConfig().getInt("settings.spawnprotectionradius")){
            e.setCancelled(true);
            Util.sendMessage(p, Lang.MESSAGES_PLACE);
            return;
        }
        if(e.getItemInHand() != null && e.getItemInHand().getType() == Material.TNT){
            TNTPrimed primed = (TNTPrimed) e.getBlockPlaced().getWorld().spawnEntity(e.getBlockPlaced().getLocation(), EntityType.PRIMED_TNT);
            primed.setFuseTicks(80);
            primed.setVelocity(primed.getVelocity().zero());
            e.setCancelled(true);
            p.getInventory().removeItem(new ItemStack(Material.TNT, 1));
            return;
        }
        getBedwars().getBlocksPlaced().add(e.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e){
        if(getBedwars().getGameStatus().equals("STARTED")){
            if(e.getBlock().getType() == Material.BED_BLOCK || e.getBlock().getType() == Material.BED){
                final User u = UserManager.getUser(e.getPlayer());
                e.getBlock().getDrops().clear();
                MetadataValue value = e.getBlock().getMetadata("team").get(0);
                TeamColor teamColor = TeamColor.valueOf(value.asString());
                if(u.getTeam().getTeamColor() == teamColor){
                    e.setCancelled(true);
                    return;
                }
                Team team = getBedwars().getTeamByColor(teamColor);
                Util.sendMessage(getBedwars().getPlayers(), Lang.MESSAGES_DESTROYBED.replace("{TEAM}", team.getTeamColor().getTeamName()));
                Util.sendMessage(team.getPlayers(), Lang.MESSAGES_DESTROYEDBED);
                Util.sendTitle(team.getPlayers(), Lang.MESSAGES_DESTROYEDBED);
                return;
            }
            if(!getBedwars().getBlocksPlaced().contains(e.getBlock().getLocation())){
                e.setCancelled(true);
                Util.sendMessage(e.getPlayer(), Lang.MESSAGES_DESTROY);
            }
        }else{
            e.setCancelled(true);
            Util.sendMessage(e.getPlayer(), Lang.MESSAGES_DESTROY);
        }
    }
}
