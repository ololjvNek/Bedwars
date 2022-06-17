package pl.ololjvNek.bedwars.data;

import lombok.Data;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.boards.BedwarsBoard;
import pl.ololjvNek.bedwars.enums.EventType;
import pl.ololjvNek.bedwars.enums.GeneratorType;
import pl.ololjvNek.bedwars.enums.TeamColor;
import pl.ololjvNek.bedwars.managers.EventManager;
import pl.ololjvNek.bedwars.managers.UserManager;
import pl.ololjvNek.bedwars.runnables.BedwarsRunnable;
import pl.ololjvNek.bedwars.utils.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
public class Bedwars {

    private String gameStatus,mapName;
    private int minPlayers;
    private long startTime,eventTime,nextEvent;
    private EventType eventType;
    private List<Player> players,spectators;
    private List<Team> teams;
    private List<Generator> generators;
    private Location lobby;
    private List<Location> blocksPlaced;
    private ScheduledExecutorService executorService;

    public Bedwars(){
        gameStatus = "WAITING";
        mapName = "bedwars";
        startTime = 0L;
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        teams = new ArrayList<>();
        generators = new ArrayList<>();
        lobby = null;
        executorService = null;
        blocksPlaced = new ArrayList<>();
        eventTime = 0L;
        nextEvent = 0L;
        eventType = null;
        minPlayers = 2;
        createTeams();
        buildMap();
    }

    public void joinToGame(Player player){
        player.getEnderChest().clear();
        player.teleport(getLobby());
        getPlayers().add(player);
        player.setHealth(20);
        player.setFoodLevel(20);
        Util.sendMessage(Bukkit.getOnlinePlayers(), Lang.MESSAGES_JOINTOGAME.replace("{PLAYER}", player.getName()).replace("{PLAYERS}", String.valueOf(getPlayers().size())).replace("{MAXPLAYERS}", String.valueOf(Bukkit.getMaxPlayers())));
        player.getInventory().clear();
        player.getInventory().setItem(4, ItemUtil.getChooseTeam().toItemStack());
        player.setScoreboard(Main.getSbBedwars());
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setFlying(false);
        if(getPlayers().size() >= getMinPlayers() && getGameStatus().equals("WAITING")){
            setGameStatus("STARTING");
            startRunnable();
            setStartTime(System.currentTimeMillis()+ TimeUtil.SECOND.getTime(Main.getGlobalConfig().getConfig().getInt("settings.game.starttime")));
        }
    }

    public void leaveGame(Player player){
        getPlayers().remove(player);
        final User u = UserManager.getUser(player);
        if(u.getTeam() != null){
            u.getTeam().removeFromTeam(player);
            u.setTeam(null);
        }
    }

    public void joinAsSpectator(Player player){
        if(getPlayers().contains(player)){
            getPlayers().remove(player);
            final User u = UserManager.getUser(player);
            u.getTeam().removeFromTeam(player);
            u.setTeam(null);
        }
        getSpectators().add(player);
        player.teleport(getLobby().clone().subtract(0, 7, 0));
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.getInventory().setItem(4, ItemUtil.getTeleportToPlayer().toItemStack());
        for(Player inGame : getPlayers()){
            if(inGame.canSee(player)){
                inGame.hidePlayer(player);
            }
        }
    }

    public void endGame(Team winner){
        new BukkitRunnable(){
            public void run(){
                setGameStatus("END");
                for(final Player p : winner.getPlayers()){
                    Util.spawnFireworks(p.getLocation(), 3);
                }
                Util.sendMessage(Bukkit.getOnlinePlayers(), Lang.MESSAGES_TEAMWIN.replace("{TEAM}", winner.getTeamColor().getTeamName()));
                new BukkitRunnable(){
                    public void run(){
                        for(final Player player : Bukkit.getOnlinePlayers()){
                            player.kickPlayer(Util.fixColors(Lang.MESSAGES_RESTARTING));
                        }
                        buildMap();
                        getGenerators().clear();
                        getSpectators().clear();
                        getPlayers().clear();
                        getBlocksPlaced().clear();
                        setGameStatus("WAITING");
                    }
                }.runTaskLater(Main.getPlugin(), 100L);
            }
        }.runTask(Main.getPlugin());
    }

    public void startRunnable(){
        new BukkitRunnable(){
            public void run(){
                if(getStartTime() > System.currentTimeMillis()){
                    Util.sendActionBar(getPlayers(), "&aGra wystartuje za &b" + DataUtil.secondsToString(getStartTime()));
                }else{
                    this.cancel();
                    startGame();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L);

    }

    public Generator searchForFurnace(GeneratorType type, TeamColor teamColor){
        for(Generator generator : getGenerators()){
            if(generator.getType() == type && generator.getTeam().getTeamColor() == teamColor){
                return generator;
            }
        }
        return null;
    }

    public Team searchForTeam(Player player){
        Team toJoin = null;
        int inTeam = 0;
        for(final Team team : getTeams()){
            if(inTeam >= team.getPlayers().size()){
                toJoin = team;
            }
            inTeam = team.getPlayers().size();
        }
        return toJoin;
    }

    public void startGame(){
        setGameStatus("STARTED");
        setStartTime(0L);
        new BedwarsRunnable(this);
        Main.getEventManager().setNextEvent(System.currentTimeMillis()+TimeUtil.MINUTE.getTime(getEventType().getEventTime()));
        for(final Player p : getPlayers()){
            final User u = UserManager.getUser(p);
            if(u.getTeam() == null){
                Team team = searchForTeam(p);
                team.addToTeam(p);
            }
        }
        for(final Team team : getTeams()) {
            if(team.getPlayers().size() == 0){
                continue;
            }
            for (final Player p : team.getPlayers()) {
                p.teleport(team.getSpawnLocation());
                team.equipPlayer(p);
                p.setGameMode(GameMode.SURVIVAL);
            }
            if(team.getBedLocation() != null){
                Util.setBed(team.getBedLocation().getWorld().getBlockAt(team.getBedLocation()), Util.getBlockFaceFromYaw(team.getBedLocation().getYaw()), Material.BED_BLOCK, team.getTeamColor());
            }
            if(team.getShopLocation() != null){
                team.setupShop();
            }
            if(team.getUpgradeLocation() != null){
                team.setupUpgrade();
            }
        }
        for(final Generator generator : getGenerators()){
            generator.activate();
            generator.setUpgrade(System.currentTimeMillis() + TimeUtil.MINUTE.getTime(1));
        }
    }

    public void buildMap(){
        World world = Bukkit.getWorld(getMapName());
        if(world == null){
            Main.getWorldManager().restoreWorldWithNames(getMapName(), getMapName());
            world = Bukkit.getWorld(getMapName());
        }else{
            Main.getWorldManager().deleteWorld(getMapName());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.getWorldManager().restoreWorldWithNames(getMapName(), getMapName());
            world = Bukkit.getWorld(getMapName());
        }

        new BukkitRunnable(){
            public void run(){
                if(Bukkit.getWorld(getMapName()) != null){
                    World w = Bukkit.getWorld(getMapName());
                    w.setGameRuleValue("doMobSpawning", "false");
                    for(final Entity entity : w.getEntities()){
                        if(entity instanceof ArmorStand){
                            setup(entity.getCustomName(), (LivingEntity)entity);
                        }
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L);
    }

    public void createTeams(){
        for(final TeamColor teamColor : TeamColor.values()){
            final Team team = new Team(teamColor);
            getTeams().add(team);
        }
    }

    public Team getTeamByColor(TeamColor color){
        for(Team team : getTeams()){
            if(team.getTeamColor() == color){
                return team;
            }
        }
        return null;
    }

    public void setup(String setup, LivingEntity entity){
        System.out.println(entity.getCustomName());
        final String[] split = setup.split("-");
        switch (split[0]){
            case "TEAM":
                final TeamColor teamColor = TeamColor.valueOf(split[1]);
                switch (teamColor){
                    case BLUE:
                    case GRAY:
                    case RED:
                    case AQUA:
                    case GREEN:
                    case WHITE:
                    case PURPLE:
                    case YELLOW:
                        switch (split[2]){
                            case "SPAWN":
                                for(final Team team : getTeams()){
                                    if(team.getTeamColor() == teamColor){
                                        team.setSpawnLocation(entity.getLocation());
                                        entity.remove();
                                        break;
                                    }
                                }
                                break;
                            case "SHOP":
                                for(final Team team : getTeams()){
                                    if(team.getTeamColor() == teamColor){
                                        team.setShopLocation(entity.getLocation());
                                        entity.remove();
                                        break;
                                    }
                                }
                                break;
                            case "UPGRADE":
                                for(final Team team : getTeams()){
                                    if(team.getTeamColor() == teamColor){
                                        team.setUpgradeLocation(entity.getLocation());
                                        entity.remove();
                                        break;
                                    }
                                }
                                break;
                            case "BED":
                                for(final Team team : getTeams()){
                                    if(team.getTeamColor() == teamColor){
                                        team.setBedLocation(entity.getLocation());
                                        entity.remove();
                                        break;
                                    }
                                }
                                break;
                            case "GENERATOR":
                                Location loc = entity.getLocation();
                                Generator generator = new Generator(loc, Arrays.asList(new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GOLD_INGOT)), 2500, GeneratorType.FURNACE);
                                generator.setTeam(getTeamByColor(teamColor));
                                getGenerators().add(generator);
                                entity.remove();
                                break;
                        }
                }
            case "ARENA":
                switch (split[1]){
                    case "GENERATOR":
                        String[] genSplit = split[2].split(":");
                        Location loc = entity.getLocation();
                        ItemStack toDrop = new ItemStack(Material.getMaterial(genSplit[0]));
                        long interval = Long.parseLong(genSplit[1]);
                        Generator generator = new Generator(loc, Collections.singletonList(toDrop), interval, replaceGeneratorsMaterials(Material.getMaterial(genSplit[0])));
                        getGenerators().add(generator);
                        entity.remove();
                        break;
                    case "SPAWN":
                        setLobby(entity.getLocation());
                        entity.remove();
                        break;
                }
        }
    }

    public GeneratorType replaceGeneratorsMaterials(Material material){
        switch (material){
            case IRON_INGOT:
                return GeneratorType.IRON;
            case GOLD_INGOT:
                return GeneratorType.GOLD;
            case DIAMOND:
                return GeneratorType.DIAMOND;
            case EMERALD:
                return GeneratorType.EMERALD;
        }
        return GeneratorType.FURNACE;
    }
}
