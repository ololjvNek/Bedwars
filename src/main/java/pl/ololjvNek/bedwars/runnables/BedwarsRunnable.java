package pl.ololjvNek.bedwars.runnables;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.ololjvNek.bedwars.boards.BedwarsBoard;
import pl.ololjvNek.bedwars.data.Bedwars;
import pl.ololjvNek.bedwars.data.Team;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BedwarsRunnable {

    @Getter private Bedwars bedwarsGame;
    private ScheduledExecutorService executorService;

    public BedwarsRunnable(Bedwars bedwars){
        bedwarsGame = bedwars;
        executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleWithFixedDelay(() -> {
           if(getBedwarsGame().getGameStatus().equals("STARTED")){
               Bukkit.getOnlinePlayers().forEach(BedwarsBoard::sendBoard);
               int aliveTeams = 0;
               for(final Team team : getBedwarsGame().getTeams()){
                   if(!team.isBedAlive() && team.getPlayers().size() <= 0){
                       continue;
                   }
                   for(final Player p : team.getPlayers()){
                       if(p != null){
                           p.setFoodLevel(20);
                       }
                   }
                   if(team.isBedAlive() || team.getPlayers().size() > 0){
                       aliveTeams++;
                   }

                   if(team.getSpeedUpgradeLevel() > 0){
                        team.speedUpgrade();
                   }
                   if(team.getArmorUpgradeLevel() > 0){
                        team.armorUpgrade();
                   }
                   if(team.getWeaponUpgradeLevel() > 0){
                       team.weaponUpgrade();
                   }
               }
               if(aliveTeams == 1){
                    for(Team team : getBedwarsGame().getTeams()){
                        if(team.getPlayers().size() > 0 && getBedwarsGame().getGameStatus().equals("STARTED")){
                            getBedwarsGame().endGame(team);
                            break;
                        }
                    }
               }
           }
        }, 1, 1, TimeUnit.SECONDS);
    }

}
