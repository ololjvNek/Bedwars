package pl.ololjvNek.bedwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.utils.Util;

public class BedwarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player) sender;
        if(args[0].equalsIgnoreCase("edit")){
            World world = new WorldCreator("bedwars-edit").environment(World.Environment.NORMAL).type(WorldType.FLAT).createWorld();
            Main.getWorldManager().restoreWorldWithNames(args[1], "bedwars-edit");
            p.teleport(world.getSpawnLocation());
        }
        if(args[0].equalsIgnoreCase("team")){
            if(args[1].equalsIgnoreCase("spawn")){
                Util.spawnArmorStand("TEAM-" + args[2] + "-SPAWN", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono lokalizacje odrodzenia teamu " + TeamColor.valueOf(args[2]).getTeamName());
            }else if(args[1].equalsIgnoreCase("upgrade")){
                Util.spawnArmorStand("TEAM-" + args[2] + "-UPGRADE", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono lokalizacje ulepszen teamu " + TeamColor.valueOf(args[2]).getTeamName());
            }else if(args[1].equalsIgnoreCase("shop")){
                Util.spawnArmorStand("TEAM-" + args[2] + "-SHOP", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono lokalizacje sklepu teamu " + TeamColor.valueOf(args[2]).getTeamName());
            }else if(args[1].equalsIgnoreCase("bed")){
                Util.spawnArmorStand("TEAM-" + args[2] + "-BED", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono lokalizacje lozka teamu " + TeamColor.valueOf(args[2]).getTeamName());
            }
        }else if(args[0].equalsIgnoreCase("generator")){
            if(args.length < 4){
                return Util.sendMessage(p, "&8->> &7Poprawne uzycie: &c/bedwars generator <other/team> <item> <interval>");
            }
            if(args[1].equalsIgnoreCase("other")){
                final String formatName = args[2].toUpperCase() + ":" + Long.parseLong(args[3]);
                Util.spawnArmorStand("ARENA-GENERATOR-" + formatName, p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono nowy generator!");
            }else if(args[1].equalsIgnoreCase("team")) {
                Util.spawnArmorStand("TEAM-" + args[2] + "-GENERATOR", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono nowy generator teamu!");
            }
        }else if(args[0].equalsIgnoreCase("global")){
            if(args[1].equalsIgnoreCase("spawn")){
                Util.spawnArmorStand("ARENA-SPAWN", p.getLocation());
                return Util.sendMessage(p, "&8->> &7Ustawiono lobby!");
            }
        }
        return false;
    }
}
