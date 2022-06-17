package pl.ololjvNek.bedwars.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.data.Team;
import pl.ololjvNek.bedwars.data.User;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.utils.ItemBuilder;
import pl.ololjvNek.bedwars.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChooseTeamProvider implements InventoryProvider {

    public static SmartInventory INVENTORY = SmartInventory.builder().manager(Main.getInventoryManager()).provider(new ChooseTeamProvider()).title(Util.fixColors("&8            * &9WYBIERZ DRUZYNE &8*")).size(1, 9).build();


    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        final User u = UserManager.getUser(player);
        int slot = 0;
        for(Team team : Main.getBedwarsGame().getTeams()){
            List<String> lore = new ArrayList<>();
            lore.add("");
            for(Player teamPlayer : team.getPlayers()){
                lore.add(Util.fixColors("&a" + teamPlayer.getName()));
            }
            ItemBuilder ib = new ItemBuilder(Material.STAINED_CLAY, 1,
                    (team.getTeamColor() == TeamColor.RED ? (byte)14 :
                            (team.getTeamColor() == TeamColor.BLUE ? (byte)11 :
                                    (team.getTeamColor() == TeamColor.GREEN ? (byte)13 :
                                            (team.getTeamColor() == TeamColor.YELLOW ? (byte)4 :
                                                    (team.getTeamColor() == TeamColor.GRAY ? (byte)7 :
                                                            (team.getTeamColor() == TeamColor.PURPLE ? (byte)10 :
                                                                    (team.getTeamColor() == TeamColor.AQUA ? (byte)3 : 0))))))))
                    .setName(Util.fixColors(team.getTeamColor().getTeamName()))
                    .setLore(lore);
            inventoryContents.set(0, slot, ClickableItem.of(ib.toItemStack(), e->{
                if(u.getTeam() == null){
                    team.addToTeam(player);
                }else if(u.getTeam().getTeamColor() != team.getTeamColor()){
                    u.getTeam().removeFromTeam(player);
                    team.addToTeam(player);
                }
                u.setTeam(team);
            }));
            slot++;
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        final User u = UserManager.getUser(player);
        int slot = 0;
        for(Team team : Main.getBedwarsGame().getTeams()){
            final List<String> lore = new ArrayList<>();
            lore.add("");
            for(Player teamPlayer : team.getPlayers()){
                lore.add(Util.fixColors("&a" + teamPlayer.getName()));
            }
            final ItemBuilder ib = new ItemBuilder(Material.STAINED_CLAY, 1,
                    (team.getTeamColor() == TeamColor.RED ? (byte)14 :
                            (team.getTeamColor() == TeamColor.BLUE ? (byte)11 :
                                    (team.getTeamColor() == TeamColor.GREEN ? (byte)13 :
                                            (team.getTeamColor() == TeamColor.YELLOW ? (byte)4 :
                                                    (team.getTeamColor() == TeamColor.GRAY ? (byte)7 :
                                                            (team.getTeamColor() == TeamColor.PURPLE ? (byte)10 :
                                                                    (team.getTeamColor() == TeamColor.AQUA ? (byte)3 : 0))))))))
                    .setName(Util.fixColors(team.getTeamColor().getTeamName()))
                    .setLore(lore);
            inventoryContents.set(0, slot, ClickableItem.of(ib.toItemStack(), e->{
                if(u.getTeam() == null){
                    team.addToTeam(player);
                }else if(u.getTeam().getTeamColor() != team.getTeamColor()){
                    u.getTeam().removeFromTeam(player);
                    team.addToTeam(player);
                }
                u.setTeam(team);
            }));
            slot++;
        }
    }
}
